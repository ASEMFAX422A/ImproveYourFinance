package org.pdf.finanzverwaltung.dto;

public class BankAccountDTO {
    private CurrencyDTO currency;
    private String iban;
    private String bic;
    private long owner;

    public BankAccountDTO(String iban, String bic, long owner, CurrencyDTO currency) {
        this.iban = iban;
        this.bic = bic;
        this.owner = owner;
        this.currency = currency;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    public long getOwner() {
        return owner;
    }

    public CurrencyDTO getCurrency() {
        return currency;
    }
}
