package lanz.global.financeservice.repository;

import lanz.global.financeservice.api.request.contract.GetContractParams;
import lanz.global.financeservice.model.Contract;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ContractFilterRepository {

    Page<Contract> findAllByFilter(UUID companyId, GetContractParams params);

}
