package lanz.global.financeservice.external.api.customer.response;

import java.util.UUID;

public record CustomerResponse(UUID customerId, String name) {
}
