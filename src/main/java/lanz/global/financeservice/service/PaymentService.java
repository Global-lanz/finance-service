package lanz.global.financeservice.service;

import lanz.global.financeservice.model.Payment;
import lanz.global.financeservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment createPayment() {
        return null;
    }

}
