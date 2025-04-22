package lanz.global.financeservice.util.converter;

import lanz.global.financeservice.api.response.invoice.UpdateInvoiceRequest;
import lanz.global.financeservice.model.Invoice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UpdateInvoiceRequestConverter implements Converter<UpdateSourceTarget<UpdateInvoiceRequest, Invoice>, Invoice> {
    @Override
    public Invoice convert(UpdateSourceTarget<UpdateInvoiceRequest, Invoice> request) {
        Invoice target = request.target();
        UpdateInvoiceRequest source = request.source();

        target.setAmount(source.amount());
        target.setDueDate(source.dueDate());
        target.setDescription(source.description());
        return target;
    }
}
