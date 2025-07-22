package lanz.global.financeservice.api.response.contract;

import lanz.global.financeservice.model.ContractStatusEnum;
import lanz.global.financeservice.model.ContractTypeEnum;
import lanz.global.financeservice.model.FrequencyEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ContractResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 7142606946855707319L;

    private UUID contractId;

    private BigDecimal totalAmount;
    private FrequencyEnum frequency;
    private Integer paymentDay;
    private LocalDate start;
    private LocalDate end;
    private ContractStatusEnum status;
    private ContractTypeEnum type;
    private String terminationClause;
    private BigDecimal penaltyFee;
    private String description;
    private ContractCustomerResponse customer;

    @Deprecated(forRemoval = true, since = "1.5.0")
    private UUID customerId;
    private UUID currencyId;
}
