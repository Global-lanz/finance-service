package lanz.global.financeservice.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.annotation.security.PermitAll;
import lanz.global.financeservice.api.response.currency.CurrencyResponse;
import lanz.global.financeservice.model.Currency;
import lanz.global.financeservice.service.CurrencyService;
import lanz.global.financeservice.util.converter.ServiceConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/finance/currency")
@RequiredArgsConstructor
public class CurrencyApi {

    private final CurrencyService currencyService;
    private final ServiceConverter serviceConverter;

    @PermitAll
    @GetMapping("/{currencyId}")
    @ApiOperation(value = "Find currency by ID", notes = "The endpoint for retrieving the currency")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Currency"), @ApiResponse(code = 404, message = "Currency not found")})
    public ResponseEntity<CurrencyResponse> findCurrencyById(@PathVariable("currencyId") UUID currencyId) {
        Currency currency = currencyService.findCurrencyById(currencyId);

        return ResponseEntity.ok(serviceConverter.convert(currency, CurrencyResponse.class));
    }

    @PermitAll
    @GetMapping
    @ApiOperation(value = "Find currencies", notes = "The endpoint for retrieving the list of currencies")
    @ApiResponse(code = 200, message = "List of currencies")
    public ResponseEntity<List<CurrencyResponse>> findCurrencies() {
        List<Currency> currencies = currencyService.findAllCurrencies();

        return ResponseEntity.ok(serviceConverter.convertList(currencies, CurrencyResponse.class));
    }

}
