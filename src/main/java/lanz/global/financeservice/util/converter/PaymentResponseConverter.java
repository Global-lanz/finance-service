package lanz.global.financeservice.util.converter;

import lanz.global.financeservice.api.response.invoice.PaymentResponse;
import lanz.global.financeservice.model.Payment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PaymentResponseConverter implements Converter<Payment, PaymentResponse> {
    @Override
    public PaymentResponse convert(Payment source) {
        return new PaymentResponse(source.getPaymentId(),
                source.getAmount(),
                source.getPaymentDate(),
                source.getNote());
    }
}
