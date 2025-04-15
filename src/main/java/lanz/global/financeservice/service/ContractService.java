package lanz.global.financeservice.service;

import lanz.global.financeservice.api.request.contract.ContractRequest;
import lanz.global.financeservice.api.request.contract.ContractStatusUpdateRequest;
import lanz.global.financeservice.exception.BadRequestException;
import lanz.global.financeservice.exception.NotFoundException;
import lanz.global.financeservice.facade.AuthenticationFacade;
import lanz.global.financeservice.model.Contract;
import lanz.global.financeservice.model.ContractStatusEnum;
import lanz.global.financeservice.model.ContractStatusTransition;
import lanz.global.financeservice.model.ContractTypeEnum;
import lanz.global.financeservice.repository.ContractRepository;
import lanz.global.financeservice.repository.ContractStatusTransitionRepository;
import lanz.global.financeservice.repository.CurrencyRepository;
import lanz.global.financeservice.util.converter.ServiceConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ContractService {

    private final ServiceConverter serviceConverter;
    private final ContractRepository contractRepository;
    private final CustomerService customerService;
    private final AuthenticationFacade authenticationFacade;
    private final ContractStatusTransitionRepository contractStatusTransitionRepository;
    private final CurrencyRepository currencyRepository;

    public Contract createContract(ContractRequest request) {
        validateCreateContract(request);

        Contract contract = serviceConverter.convert(request, Contract.class);
        contract.setStatus(ContractStatusEnum.QUOTATION);
        contract.setType(ContractTypeEnum.QUOTE);
        contract.setCompanyId(authenticationFacade.getCompanyId());
        return contractRepository.save(contract);
    }

    public Contract findContractById(UUID contractId) {
        UUID companyId = authenticationFacade.getCompanyId();
        return contractRepository.findByContractIdAndCompanyId(contractId, companyId).orElseThrow(() -> new NotFoundException("Contract"));
    }

    public List<Contract> findAllContracts() {
        UUID companyId = authenticationFacade.getCompanyId();
        return contractRepository.findAllByCompanyId(companyId);
    }

    public void deleteContractById(UUID contractId) {
        Contract contract = findContractById(contractId);
        validateDeleteContract(contract);
        contractRepository.delete(contract);
    }

    public Contract updateContractStatus(UUID contractId, ContractStatusUpdateRequest request) {
        Contract contract = findContractById(contractId);

        validateUpdateContractStatus(contract.getStatus(), request.status());

        contract.setStatus(request.status());

        if (contract.isRunning()) {
            ContractTypeEnum contractType = switch (contract.getType()) {
                case QUOTE -> ContractTypeEnum.CONTRACT;
                case AMENDMENT_QUOTE -> ContractTypeEnum.AMENDMENT_CONTRACT;
                case CANCELLATION_QUOTE -> ContractTypeEnum.CANCELLATION_CONTRACT;
                default -> null;
            };

            if (contractType != null) {
                contract.setType(contractType);
            }
        }

        return contractRepository.save(contract);
    }

    public Contract updateContract(UUID contractId, ContractRequest request) {
        Contract contract = findContractById(contractId);
        validateUpdateContract(contract, request);

        Contract updatedContract = serviceConverter.convert(request, contract, Contract.class);

        return contractRepository.save(updatedContract);
    }

    private void validateDeleteContract(Contract contract) {
        var permittedTypes = List.of(ContractTypeEnum.QUOTE, ContractTypeEnum.AMENDMENT_QUOTE, ContractTypeEnum.CANCELLATION_QUOTE);
        if (!permittedTypes.contains(contract.getType())) {
            throw new BadRequestException("exception.delete-contract-type-not-allowed.title", "exception.delete-contract-type-not-allowed.description", contract.getType().name());
        }

        var permittedStatus = List.of(ContractStatusEnum.QUOTATION, ContractStatusEnum.AWAITING_SIGNATURE, ContractStatusEnum.APPROVED);
        if (!permittedStatus.contains(contract.getStatus())) {
            throw new BadRequestException("exception.delete-contract-status-not-allowed.title", "exception.delete-contract-status-not-allowed.description", contract.getStatus().name());
        }
    }

    private void validateUpdateContract(Contract contract, ContractRequest request) {
        var permittedTypes = List.of(ContractTypeEnum.QUOTE, ContractTypeEnum.AMENDMENT_QUOTE, ContractTypeEnum.CANCELLATION_QUOTE);
        if (!permittedTypes.contains(contract.getType())) {
            throw new BadRequestException("exception.update-contract-type-not-allowed.title", "exception.update-contract-type-not-allowed.description", contract.getType().name());
        }

        var permittedStatus = List.of(ContractStatusEnum.QUOTATION);
        if (!permittedStatus.contains(contract.getStatus())) {
            throw new BadRequestException("exception.update-contract-status-not-allowed.title", "exception.update-contract-status-not-allowed.description", contract.getStatus().name());
        }

        validateCurrencyExists(request.currencyId());
    }

    private void validateCurrencyExists(UUID currencyId) {
        currencyRepository.findById(currencyId).orElseThrow(() -> new NotFoundException("Currency"));
    }

    private void validateUpdateContractStatus(ContractStatusEnum fromStatus, ContractStatusEnum toStatus) {
        Optional<ContractStatusTransition> contractStatusTransition = contractStatusTransitionRepository.findByFromStatusAndToStatus(fromStatus, toStatus);

        if (contractStatusTransition.isEmpty()) {
            throw new BadRequestException("exception.update-contract-status-from-to-not-allowed.title", "exception.update-contract-status-from-to-not-allowed.description", fromStatus.name(), toStatus.name());
        }
    }

    private void validateCreateContract(ContractRequest request) {
        validateCustomerExists(request.customerId());
        validateCurrencyExists(request.currencyId());
    }

    private void validateCustomerExists(UUID customerId) {
        customerService.findCustomerById(customerId);
    }

    public List<ContractStatusTransition> findAllContractStatusTransitions() {
        return contractStatusTransitionRepository.findAll();
    }
}
