package lanz.global.financeservice.util.converter;

import lanz.global.financeservice.api.response.contract.ContractCustomerResponse;
import lanz.global.financeservice.api.response.contract.ContractResponse;
import lanz.global.financeservice.external.api.customer.response.CustomerResponse;
import lanz.global.financeservice.model.Contract;
import lanz.global.financeservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractResponseConverter implements Converter<Contract, ContractResponse> {

    private final CustomerService customerService;

    @Override
    public ContractResponse convert(Contract source) {
        ContractResponse response = new ContractResponse();
        response.setContractId(source.getContractId());
        response.setCustomerId(source.getCustomerId());
        response.setTotalAmount(source.getTotalAmount());
        response.setFrequency(source.getFrequency());
        response.setPaymentDay(source.getPaymentDay());
        response.setStart(source.getStart());
        response.setEnd(source.getEnd());
        response.setStatus(source.getStatus());
        response.setType(source.getType());
        response.setTerminationClause(source.getTerminationClause());
        response.setPenaltyFee(source.getPenaltyFee());
        response.setDescription(source.getDescription());
        response.setCurrencyId(source.getCurrencyId());

        CustomerResponse customerResponse = customerService.findCustomerById(source.getCustomerId());

        response.setCustomer(new ContractCustomerResponse(customerResponse.customerId(), customerResponse.name()));

        return response;
    }
}
