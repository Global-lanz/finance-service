package lanz.global.financeservice.repository.impl;

import lanz.global.financeservice.api.request.contract.GetContractParams;
import lanz.global.financeservice.model.Contract;
import lanz.global.financeservice.repository.ContractRepository;
import lanz.global.financeservice.repository.specification.ContractSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ContractRepositoryFilter {

    private final ContractRepository contractRepository;

    public Page<Contract> findAllByFilter(UUID companyId, GetContractParams params) {
        Specification<Contract> spec = Specification.where(ContractSpecification.customerId(params.getCustomerId()))
                .and(ContractSpecification.companyId(companyId));

        Pageable pageable = PageRequest.of(params.getPage(), params.getSize());

        return contractRepository.findAll(spec, pageable);
    }

}
