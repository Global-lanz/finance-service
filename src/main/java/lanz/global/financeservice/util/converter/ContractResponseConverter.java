package lanz.global.financeservice.util.converter;

import lanz.global.financeservice.api.response.contract.ContractResponse;
import lanz.global.financeservice.model.Contract;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ContractResponseConverter implements Converter<Contract, ContractResponse> {
    @Override
    public ContractResponse convert(Contract source) {
        return new ContractResponse(source.getContractId(), source.getCustomerId(),
                source.getTotalAmount(),
                source.getFrequency(),
                source.getPaymentDay(),
                source.getStart(),
                source.getEnd(),
                source.getStatus(),
                source.getType(),
                source.getTerminationClause(),
                source.getPenaltyFee(),
                source.getCurrencyId());
    }
}
