package lanz.global.financeservice.service;

import lanz.global.financeservice.exception.BadRequestException;
import lanz.global.financeservice.model.Currency;
import lanz.global.financeservice.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public Currency findCurrencyById(UUID currencyId) throws BadRequestException {
        if (currencyId == null) {
            return null;
        }

        return currencyRepository.findById(currencyId).orElseThrow(() -> new BadRequestException("exception.not-found.title", "exception.not-found.message", "Currency"));
    }

    public List<Currency> findAllCurrencies() {
        return currencyRepository.findAll();
    }
}
