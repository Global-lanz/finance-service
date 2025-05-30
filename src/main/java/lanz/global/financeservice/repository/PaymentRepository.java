package lanz.global.financeservice.repository;

import lanz.global.financeservice.model.Invoice;
import lanz.global.financeservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByInvoice(Invoice invoice);
}
