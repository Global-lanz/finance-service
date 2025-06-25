package lanz.global.financeservice.api.response.contract;

import java.util.UUID;

public record ContractCustomerResponse(UUID customerId, String name) {
}
