package lanz.global.financeservice.api.response.contract;

import lanz.global.financeservice.model.ContractStatusEnum;
import lanz.global.financeservice.model.ContractTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContractResponse(UUID contractId,
                               UUID customerId,
                               BigDecimal totalAmount,
                               String frequency,
                               Integer paymentDay,
                               LocalDate start,
                               LocalDate end,
                               ContractStatusEnum status,
                               ContractTypeEnum type,
                               String terminationClause,
                               BigDecimal penaltyFee,
                               String description,
                               UUID currencyId) implements Serializable {
}
