package lanz.global.financeservice.util.converter;

import lanz.global.financeservice.api.request.contract.ContractRequest;
import lanz.global.financeservice.model.Contract;
import lanz.global.libraryservice.converter.dto.UpdateSourceTarget;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UpdateContractRequestConverter implements Converter<UpdateSourceTarget<ContractRequest, Contract>, Contract> {

    @Override
    public Contract convert(UpdateSourceTarget<ContractRequest, Contract> request) {
        Contract target = request.target();
        ContractRequest source = request.source();
        target.setTotalAmount(source.totalAmount());
        target.setFrequency(source.frequency());
        target.setPaymentDay(source.paymentDay());
        target.setStart(source.start());
        target.setEnd(source.end());
        target.setTerminationClause(source.terminationClause());
        target.setPenaltyFee(source.penaltyFee());
        target.setDescription(source.description());
        target.setWeekPaymentDay(source.weekPaymentDay());
        target.setCustomerId(source.customerId());
        target.setCurrencyId(source.currencyId());
        return target;
    }

}
