package lanz.global.financeservice.external.api.response;

import java.util.UUID;

public record CustomerResponse(UUID customerId, String name) {
}
