package lanz.global.financeservice.api.response.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PaymentResponse(UUID paymentId,
                              BigDecimal amount,
                              LocalDate paymentDate,
                              String note) {
}
