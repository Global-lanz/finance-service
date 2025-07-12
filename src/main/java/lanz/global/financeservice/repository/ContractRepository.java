package lanz.global.financeservice.repository;

import lanz.global.financeservice.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends ContractFilterRepository, JpaRepository<Contract, UUID> {

    Optional<Contract> findByContractIdAndCompanyId(UUID contractId, UUID companyId);

}
