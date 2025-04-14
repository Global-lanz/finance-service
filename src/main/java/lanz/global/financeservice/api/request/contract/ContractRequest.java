package lanz.global.financeservice.api.request.contract;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContractRequest(@Schema(description = "The ID of the customer") @NotNull UUID customerId,
                              @Schema(description = "The total amount of the contract", example = "299.90") @NotNull BigDecimal totalAmount,
                              @Schema(description = "The frequency of the payment", example = "MONTHLY") String frequency,
                              @Schema(description = "The ID of the customer", example = "10") @Size(max = 31, min = 1) Integer paymentDay,
                              @Schema(description = "The ID of the customer", example = "2025-01-15") LocalDate start,
                              @Schema(description = "The ID of the customer", example = "2025-07-15") LocalDate end,
                              @Schema(description = "The ID of the customer") String terminationClause,
                              @Schema(description = "The ID of the customer") @Size(max = 100) BigDecimal penaltyFee,
                              @Schema(description = "The ID of the customer") UUID currencyId) implements Serializable {
}
