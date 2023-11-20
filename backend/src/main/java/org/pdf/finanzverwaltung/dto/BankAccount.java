package org.pdf.finanzverwaltung.dto;

public class BankAccount {
    private Currency currency;
    private String iban;
    private String bic;
    private long owner;

    public BankAccount(String iban, String bic, long owner, Currency currency) {
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

    public Currency getCurrency() {
        return currency;
    }
}
