package lanz.global.financeservice.repository;

import lanz.global.financeservice.api.request.contract.GetContractParams;
import lanz.global.financeservice.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContractFilterRepository {

    Page<Contract> findAllByFilter(UUID companyId, GetContractParams params);

}
