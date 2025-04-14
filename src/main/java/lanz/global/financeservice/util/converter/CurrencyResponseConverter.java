package lanz.global.financeservice.util.converter;

import lanz.global.financeservice.api.response.currency.CurrencyResponse;
import lanz.global.financeservice.model.Currency;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CurrencyResponseConverter implements Converter<Currency, CurrencyResponse> {
    @Override
    public CurrencyResponse convert(Currency entity) {

        return new CurrencyResponse(
                entity.getCurrencyId(),
                entity.getName(),
                entity.getCode(),
                entity.getSymbol()
        );
    }
}
