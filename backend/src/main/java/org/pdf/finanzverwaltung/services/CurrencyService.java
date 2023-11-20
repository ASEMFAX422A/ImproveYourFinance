package org.pdf.finanzverwaltung.services;

import java.util.Optional;

import org.pdf.finanzverwaltung.dto.Currency;
import org.pdf.finanzverwaltung.models.DCurrency;
import org.pdf.finanzverwaltung.repos.currency.CurrencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepo currencyRepo;

    public CurrencyService() {
    }

    public Currency dCurrencyToCurrency(DCurrency currency) {
        return new Currency(currency.getShortName(), currency.getLongName());
    }

    public DCurrency currencyToDCurrency(Currency currency) {
        Optional<DCurrency> curOpt = currencyRepo.findByShortName(currency.getShortName());
        if (curOpt.isPresent())
            return curOpt.get();

        return new DCurrency(currency.getShortName(), currency.getLongName());
    }
}
