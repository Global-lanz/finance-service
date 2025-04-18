package lanz.global.financeservice.api.request.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentRequest(@Schema(description = "The amount paid", example = "299.90") @NotNull BigDecimal amount,
                             @Schema(description = "The date of the payment", example = "2025-04-10") @NotNull LocalDate paymentDate,
                             @Schema(description = "The note of the payment", example = "Paid by bank transfer") String note) {
}
