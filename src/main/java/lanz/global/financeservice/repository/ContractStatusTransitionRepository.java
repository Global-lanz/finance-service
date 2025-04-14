package lanz.global.financeservice.repository;

import lanz.global.financeservice.model.ContractStatusEnum;
import lanz.global.financeservice.model.ContractStatusTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractStatusTransitionRepository extends JpaRepository<ContractStatusTransition, UUID> {

    Optional<ContractStatusTransition> findByFromStatusAndToStatus(ContractStatusEnum fromStatus, ContractStatusEnum toStatus);

}
