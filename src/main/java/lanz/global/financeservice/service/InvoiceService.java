package lanz.global.financeservice.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lanz.global.financeservice.api.request.payment.PaymentRequest;
import lanz.global.financeservice.api.response.InvoiceFileResponse;
import lanz.global.financeservice.api.response.invoice.CreateInvoiceRequest;
import lanz.global.financeservice.api.response.invoice.UpdateInvoiceRequest;
import lanz.global.financeservice.exception.BadRequestException;
import lanz.global.financeservice.exception.InvoicePDFNotFound;
import lanz.global.financeservice.external.api.customer.response.CustomerResponse;
import lanz.global.financeservice.facade.impl.AuthenticationFacadeImpl;
import lanz.global.financeservice.model.Contract;
import lanz.global.financeservice.model.ContractStatusEnum;
import lanz.global.financeservice.model.ContractTypeEnum;
import lanz.global.financeservice.model.Currency;
import lanz.global.financeservice.model.FrequencyEnum;
import lanz.global.financeservice.model.Invoice;
import lanz.global.financeservice.model.Payment;
import lanz.global.financeservice.repository.ContractRepository;
import lanz.global.financeservice.repository.InvoiceRepository;
import lanz.global.financeservice.repository.PaymentRepository;
import lanz.global.financeservice.util.converter.ServiceConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ContractRepository contractRepository;
    private final AuthenticationFacadeImpl authenticationFacade;
    private final PaymentRepository paymentRepository;
    private final ContractService contractService;
    private final ServiceConverter serviceConverter;
    private final CustomerService customerService;
    private final CurrencyService currencyService;
    private final Configuration freemarkerConfig;
    private final S3Client s3Client;

    public void createInvoices(UUID contractId) {
        log.info("INVOICE-GENERATION: Started invoice generation for contract {}", contractId);
        Optional<Contract> optionalContract = contractRepository.findById(contractId);

        if (optionalContract.isEmpty()) {
            log.error("INVOICE-GENERATION: Failed, the contract with ID {} was not found", contractId);
            return;
        }

        Contract contract = optionalContract.get();

        var quotationStatus = List.of(ContractTypeEnum.QUOTE, ContractTypeEnum.AMENDMENT_QUOTE, ContractTypeEnum.CANCELLATION_QUOTE);

        if (quotationStatus.contains(contract.getType()) && contract.getStatus().equals(ContractStatusEnum.APPROVED)) {
            List<Invoice> invoices = new ArrayList<>();

            UUID companyId = contract.getCompanyId();
            int installmentQuantity = extractInstallmentQuantity(contract);
            int currentInvoiceNumber = getCurrentInvoiceNumber(contract);
            BigDecimal installmentAmount = getInstallmentAmount(contract, installmentQuantity);
            LocalDate startDateReference = getStartDateReference(contract);

            for (int i = 1; i <= installmentQuantity; i++) {
                int invoiceNumber = currentInvoiceNumber + i;
                Invoice invoice = createInvoice(companyId, installmentAmount, invoiceNumber, startDateReference, contract.getFrequency());
                invoice.setContract(contract);

                generateInvoicePdf(invoice);
                invoices.add(invoice);
            }

            invoiceRepository.saveAll(invoices);
            updateContractTypeToEffective(contract);
            contractRepository.save(contract);
        }

        log.info("INVOICE-GENERATION: Ended invoice generation for contract {}", contractId);
    }

    private void updateContractTypeToEffective(Contract contract) {
        ContractTypeEnum contractType = switch (contract.getType()) {
            case QUOTE -> ContractTypeEnum.CONTRACT;
            case AMENDMENT_QUOTE -> ContractTypeEnum.AMENDMENT_CONTRACT;
            case CANCELLATION_QUOTE -> ContractTypeEnum.CANCELLATION_CONTRACT;
            default -> null;
        };

        if (contractType != null) {
            contract.setType(contractType);
        }
    }

    private Integer extractInstallmentQuantity(Contract contract) {
        return contract.getFrequency()
                .calculateInstallmentQuantity(contract.getStart(), contract.getEnd());
    }

    private BigDecimal getInstallmentAmount(Contract contract, int installmentQuantity) {
        return contract.getTotalAmount().divide(BigDecimal.valueOf(installmentQuantity), new MathContext(2));
    }

    private LocalDate getStartDateReference(Contract contract) {
        return contract.getFrequency().calculateStartDateReference(
                contract.getStart(),
                contract.getPaymentDay(),
                contract.getWeekPaymentDay()
        );
    }

    private Invoice createInvoice(UUID companyId, BigDecimal invoiceAmount, int invoiceNumber, LocalDate startDateReference, FrequencyEnum frequency) {
        Invoice invoice = new Invoice();
        invoice.setCompanyId(companyId);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setAmount(invoiceAmount);
        invoice.setDueDate(frequency.calculateDueDate(startDateReference, invoiceNumber));
        return invoice;
    }

    public void deleteUnpaidInvoices(UUID contractId) {
        Optional<Contract> optionalContract = contractRepository.findById(contractId);

        if (optionalContract.isEmpty()) {
            return;
        }
        Contract contract = optionalContract.get();
        List<Invoice> invoices = findUnpaidInvoices(contract);

        invoiceRepository.deleteAll(invoices);
    }

    private List<Invoice> findUnpaidInvoices(Contract contract) {
        List<Invoice> invoices = invoiceRepository.findAllByContract(contract);

        if (invoices.isEmpty()) {
            return invoices;
        }

        return invoices.stream()
                .filter(invoice -> Objects.isNull(invoice.getPayment()))
                .toList();
    }

    public Payment updatePayment(UUID invoiceId, PaymentRequest request) {
        Invoice invoice = findInvoiceById(invoiceId);

        Payment payment = invoice.getPayment();

        if (payment == null) {
            payment = new Payment();
            payment.setInvoice(invoice);
            payment.setCompanyId(invoice.getCompanyId());
        }

        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setNote(request.note());

        return paymentRepository.save(payment);
    }

    public Invoice findInvoiceById(UUID invoiceId) {
        UUID companyId = authenticationFacade.getCompanyId();
        return invoiceRepository.findByInvoiceIdAndCompanyId(invoiceId, companyId).orElseThrow(() -> new BadRequestException("invoice"));
    }

    public Payment getPaymentByInvoiceId(UUID invoiceId) {
        Invoice invoice = findInvoiceById(invoiceId);
        return invoice.getPayment();
    }

    public void deletePaymentBy(UUID invoiceId) {
        Invoice invoice = findInvoiceById(invoiceId);
        Optional<Payment> optionalPayment = paymentRepository.findByInvoice(invoice);
        invoice.setPayment(null);
        invoiceRepository.save(invoice);
        optionalPayment.ifPresent(paymentRepository::delete);
    }

    public List<Invoice> findInvoicesByContractId(UUID contractId) {
        Contract contract = contractService.findContractById(contractId);
        return invoiceRepository.findAllByContract(contract);
    }

    public Invoice createInvoice(CreateInvoiceRequest request) {
        Contract contract = contractService.findContractById(request.contractId());
        Invoice invoice = serviceConverter.convert(request, Invoice.class);

        invoice.setContract(contract);
        invoice.setCompanyId(contract.getCompanyId());
        invoice.setInvoiceNumber(generateNextInvoiceNumber(contract));

        return generateInvoicePdfAndSave(invoice);
    }

    public Invoice updateInvoice(UUID invoiceId, UpdateInvoiceRequest request) {
        Invoice invoice = findInvoiceById(invoiceId);
        Invoice updatedInvoice = serviceConverter.convert(request, invoice, Invoice.class);

        return generateInvoicePdfAndSave(updatedInvoice);
    }

    private Invoice generateInvoicePdfAndSave(Invoice invoice) {
        try {
            generateInvoicePdf(invoice);
            return invoiceRepository.save(invoice);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void generateInvoicePdf(Invoice invoice) {
        try {
            String fileName = generatePdf(invoice);
            invoice.setFile(fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generatePdf(Invoice invoice) throws Exception {
        Contract contract = invoice.getContract();
        CustomerResponse customer = customerService.findCustomerById(contract.getCustomerId());
        Currency currency = currencyService.findCurrencyById(contract.getCurrencyId());

        Map<String, Object> data = new HashMap<>();
        data.put("invoiceNumber", invoice.getInvoiceNumber());
        data.put("contractId", contract.getContractId());
        data.put("customer", customer.name());
        data.put("dueDate", invoice.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        data.put("currency", currency.getSymbol());
        data.put("total", invoice.getAmount());
        data.put("description", invoice.getDescription());

        String language = getLocale().getLanguage();
        String templatePath = String.format("invoice/%s/%s.ftl", language, "invoice");

        Template template = freemarkerConfig.getTemplate(templatePath);
        StringWriter stringWriter = new StringWriter();
        template.process(data, stringWriter);

        byte[] file = generatePdfFromHtml(stringWriter.toString());

        String htmlFileName = String.format("%s/%s/%s.pdf", customer.customerId().toString(), contract.getContractId().toString(), invoice.getInvoiceNumber().toString());

        return sendToAmazonS3("invoices", htmlFileName, file);
    }

    public byte[] generatePdfFromHtml(String html) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(html, null); // baseUri = null se não usa imagens externas
        builder.toStream(outputStream);
        builder.run();

        return outputStream.toByteArray();
    }

    private String sendToAmazonS3(String bucket, String fileName, byte[] pdfBytes) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType("application/pdf")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pdfBytes));
        return fileName;
    }

    private Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    private Integer getCurrentInvoiceNumber(Contract contract) {
        return invoiceRepository.findCurrentInvoiceNumber(contract.getContractId()).orElse(0);
    }

    private Integer generateNextInvoiceNumber(Contract contract) {
        int currentInvoiceNumber = invoiceRepository.findCurrentInvoiceNumber(contract.getContractId()).orElse(0);
        return currentInvoiceNumber + 1;
    }

    public InvoiceFileResponse getInvoiceFile(UUID invoiceId) {
        Invoice invoice = findInvoiceById(invoiceId);
        return getInvoiceFilePDF(invoice.getFile());
    }

    private InvoiceFileResponse getInvoiceFilePDF(String file) {

        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket("invoices")
                    .key(file)
                    .build();

            String fileName = file.substring(file.lastIndexOf("/"));
            byte[] fileContent = s3Client.getObject(request, ResponseTransformer.toBytes()).asByteArray();

            return new InvoiceFileResponse(fileName, fileContent);
        } catch (NoSuchKeyException e) {
            throw new InvoicePDFNotFound();
        }
    }
}
