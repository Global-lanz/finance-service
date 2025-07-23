package lanz.global.financeservice.util.converter;

import lanz.global.financeservice.api.request.contract.ContractRequest;
import lanz.global.financeservice.model.Contract;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ContractRequestConverter implements Converter<ContractRequest, Contract> {
    @Override
    public Contract convert(ContractRequest source) {
        Contract contract = new Contract();
        contract.setTotalAmount(source.totalAmount());
        contract.setFrequency(source.frequency());
        contract.setPaymentDay(source.paymentDay());
        contract.setStart(source.start());
        contract.setEnd(source.end());
        contract.setTerminationClause(source.terminationClause());
        contract.setPenaltyFee(source.penaltyFee());
        contract.setDescription(source.description());
        contract.setWeekPaymentDay(source.weekPaymentDay());
        contract.setCustomerId(source.customerId());
        contract.setCurrencyId(source.currencyId());
        return contract;
    }
}
