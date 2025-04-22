package lanz.global.financeservice.util.converter;

import lanz.global.financeservice.api.response.invoice.CreateInvoiceRequest;
import lanz.global.financeservice.model.Invoice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateInvoiceRequestConverter implements Converter<CreateInvoiceRequest, Invoice> {
    @Override
    public Invoice convert(CreateInvoiceRequest source) {
        Invoice target = new Invoice();
        target.setAmount(source.amount());
        target.setDueDate(source.dueDate());
        target.setDescription(source.description());
        return target;
    }
}
