package lanz.global.financeservice.repository;

import lanz.global.financeservice.model.Contract;
import lanz.global.financeservice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    List<Invoice> findAllByContract(Contract contract);

    Optional<Invoice> findByInvoiceIdAndCompanyId(UUID invoiceId, UUID companyId);
}
