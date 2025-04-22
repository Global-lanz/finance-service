package lanz.global.financeservice.service;

import lanz.global.financeservice.api.request.payment.PaymentRequest;
import lanz.global.financeservice.api.response.invoice.CreateInvoiceRequest;
import lanz.global.financeservice.api.response.invoice.UpdateInvoiceRequest;
import lanz.global.financeservice.exception.BadRequestException;
import lanz.global.financeservice.facade.impl.AuthenticationFacadeImpl;
import lanz.global.financeservice.model.Contract;
import lanz.global.financeservice.model.ContractStatusEnum;
import lanz.global.financeservice.model.ContractTypeEnum;
import lanz.global.financeservice.model.Invoice;
import lanz.global.financeservice.model.Payment;
import lanz.global.financeservice.repository.ContractRepository;
import lanz.global.financeservice.repository.InvoiceRepository;
import lanz.global.financeservice.repository.PaymentRepository;
import lanz.global.financeservice.util.converter.ServiceConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ContractRepository contractRepository;
    private final AuthenticationFacadeImpl authenticationFacade;
    private final PaymentRepository paymentRepository;
    private final ContractService contractService;
    private final ServiceConverter serviceConverter;

    public void createInvoices(UUID contractId) {
        Optional<Contract> optionalContract = contractRepository.findById(contractId);

        if (optionalContract.isEmpty()) {
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
                Invoice invoice = createInvoice(companyId, installmentAmount, invoiceNumber, startDateReference);
                invoice.setContract(contract);
                invoices.add(invoice);
            }

            invoiceRepository.saveAll(invoices);
            updateContractTypeToEffective(contract);
            contractRepository.save(contract);
        }

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
        LocalDate startDate = contract.getStart();
        LocalDate endDate = contract.getEnd();

        return Period.between(startDate, endDate).getMonths();
    }

    private BigDecimal getInstallmentAmount(Contract contract, int installmentQuantity) {
        return contract.getTotalAmount().divide(BigDecimal.valueOf(installmentQuantity), new MathContext(2));
    }

    private LocalDate getStartDateReference(Contract contract) {
        LocalDate reference = contract.getStart().withDayOfMonth(contract.getPaymentDay());
        return reference.isAfter(contract.getStart()) ? reference : reference.plusMonths(1);
    }

    private Invoice createInvoice(UUID companyId, BigDecimal invoiceAmount, int invoiceNumber, LocalDate startDateReference) {
        Invoice invoice = new Invoice();
        invoice.setCompanyId(companyId);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setAmount(invoiceAmount);
        invoice.setDueDate(startDateReference.plusMonths(invoiceNumber));
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

    private Invoice findInvoiceById(UUID invoiceId) {
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

        return invoiceRepository.save(invoice);
    }

    private Integer getCurrentInvoiceNumber(Contract contract) {
        return invoiceRepository.findCurrentInvoiceNumber(contract.getContractId()).orElse(0);
    }

    private Integer generateNextInvoiceNumber(Contract contract) {
        int currentInvoiceNumber = invoiceRepository.findCurrentInvoiceNumber(contract.getContractId()).orElse(0);
        return currentInvoiceNumber + 1;
    }

    public Invoice updateInvoice(UUID invoiceId, UpdateInvoiceRequest request) {
        Invoice invoice = findInvoiceById(invoiceId);

        Invoice updatedInvoice = serviceConverter.convert(request, invoice, Invoice.class);

        return invoiceRepository.save(updatedInvoice);
    }
}
