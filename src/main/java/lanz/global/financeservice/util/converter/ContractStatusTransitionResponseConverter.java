package lanz.global.financeservice.util.converter;

import lanz.global.financeservice.api.response.contract.ContractStatusTransitionResponse;
import lanz.global.financeservice.model.ContractStatusTransition;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ContractStatusTransitionResponseConverter implements Converter<ContractStatusTransition, ContractStatusTransitionResponse> {
    @Override
    public ContractStatusTransitionResponse convert(ContractStatusTransition source) {
        return new ContractStatusTransitionResponse(source.getFromStatus(), source.getToStatus());
    }
}
