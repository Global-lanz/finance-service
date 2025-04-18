package lanz.global.financeservice.api.response.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class InvoiceResponse {

    public UUID invoiceId;
    public Integer invoiceNumber;
    public BigDecimal amount;
    public LocalDate dueDate;
    public String description;
    public boolean paid

    public PaymentResponse paymentResponse;
}
