package lanz.global.financeservice.util.converter;

import lanz.global.financeservice.api.response.invoice.InvoiceResponse;
import lanz.global.financeservice.api.response.invoice.PaymentResponse;
import lanz.global.financeservice.model.Invoice;
import lanz.global.libraryservice.converter.component.ServiceConverter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InvoiceResponseConverter implements Converter<Invoice, InvoiceResponse> {

    private final ServiceConverter converter;

    public InvoiceResponseConverter(@Lazy ServiceConverter converter) {
        this.converter = converter;
    }

    @Override
    public InvoiceResponse convert(Invoice source) {
        InvoiceResponse response = new InvoiceResponse();
        response.invoiceId = source.getInvoiceId();
        response.amount = source.getAmount();
        response.description = source.getDescription();
        response.invoiceNumber = source.getInvoiceNumber();
        response.dueDate = source.getDueDate();
        response.paymentResponse = converter.convert(source.getPayment(), PaymentResponse.class);
        response.paid = response.paymentResponse != null && response.paymentResponse.paymentDate() != null;
        return response;
    }
}
