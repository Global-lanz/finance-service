package lanz.global.financeservice.api.request.contract;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContractRequest(@Schema(description = "The ID of the customer") @NotNull UUID customerId,
                              @Schema(description = "The total amount of the contract", example = "299.90") @NotNull BigDecimal totalAmount,
                              @Schema(description = "The frequency of the payment", example = "MONTHLY") String frequency,
                              @Schema(description = "The day of the payment", example = "10") @DecimalMax(value = "31") @DecimalMin(value = "1") Integer paymentDay,
                              @Schema(description = "The start date of the contract", example = "2025-01-15") LocalDate start,
                              @Schema(description = "The end date of the contract", example = "2025-07-15") LocalDate end,
                              @Schema(description = "The termination clause of the contract in case of an early termination") String terminationClause,
                              @Schema(description = "The penalty fee of the contract in case of an early termination") @Digits(integer = 3, fraction = 2) BigDecimal penaltyFee,
                              @Schema(description = "The short description of the contract for identification") String description,
                              @Schema(description = "The ID of the currency") UUID currencyId) implements Serializable {
}
