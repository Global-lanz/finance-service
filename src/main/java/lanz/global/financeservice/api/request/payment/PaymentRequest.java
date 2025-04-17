package lanz.global.financeservice.api.request.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PaymentRequest(@Schema(description = "The ID of the invoice") @NotNull UUID invoiceId,
                             @Schema(description = "The amount paid", example = "299.90") @NotNull BigDecimal totalAmount,
                             @Schema(description = "The date of the payment", example = "2025-04-10") @NotNull LocalDate paymentDate) {
}
