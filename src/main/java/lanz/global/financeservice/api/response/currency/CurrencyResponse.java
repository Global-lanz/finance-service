package lanz.global.financeservice.api.response.currency;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CurrencyResponse(@Schema(description = "The id of the currency") UUID currencyId,
                               @Schema(description = "The description of the currency") String name,
                               @Schema(description = "The code of the currency") String code,
                               @Schema(description = "The symbol of the currency") String symbol) {
}
