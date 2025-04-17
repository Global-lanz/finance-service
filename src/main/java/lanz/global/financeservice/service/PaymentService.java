package lanz.global.financeservice.service;

import lanz.global.financeservice.model.Payment;
import lanz.global.financeservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentService {

    private final PaymentRepository paymentRepository;


    public Payment createPayment() {

    }

}
