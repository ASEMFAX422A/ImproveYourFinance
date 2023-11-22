package org.pdf.finanzverwaltung.services;

import org.pdf.finanzverwaltung.dto.CurrencyDTO;
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

    public CurrencyDTO dCurrencyToCurrency(DCurrency currency) {
        if (currency == null)
            return null;

        return new CurrencyDTO(currency.getShortName(), currency.getLongName());
    }

    public DCurrency currencyToDCurrency(CurrencyDTO currency) {
        if (currency == null)
            return null;

        final DCurrency cur = currencyRepo.findByShortName(currency.shortName);
        if (cur != null)
            return cur;

        return new DCurrency(currency.shortName, currency.longName);
    }
}
