package lanz.global.financeservice.api.response.invoice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateInvoiceRequest(@Schema(description = "The amount of the invoice") @NotNull BigDecimal amount,
                                   @Schema(description = "The last day for payment ") @NotNull LocalDate dueDate,
                                   @Schema(description = "The user account ID") @NotEmpty String description) {
}
