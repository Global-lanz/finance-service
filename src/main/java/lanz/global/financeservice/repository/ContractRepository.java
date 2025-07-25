package lanz.global.financeservice.repository;

import lanz.global.financeservice.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID>, JpaSpecificationExecutor<Contract> {

    Optional<Contract> findByContractIdAndCompanyId(UUID contractId, UUID companyId);

    List<Contract> findAllByCompanyId(UUID companyId);

}
