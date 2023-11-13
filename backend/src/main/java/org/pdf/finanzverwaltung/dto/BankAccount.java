package org.pdf.finanzverwaltung.dto;

import org.pdf.finanzverwaltung.repos.bank.DBankAccount;
import org.pdf.finanzverwaltung.repos.currency.DCurrency;

public class BankAccount {
    private DCurrency currency;
    private String iban;
    private String bic;
    private long owner;

    public BankAccount(String iban, String bic, long owner, DCurrency currency) {
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

    public DCurrency getCurrency() {
        return currency;
    }

    public static BankAccount create(DBankAccount account) {
        return new BankAccount(account.getIban(), account.getBic(), account.getUser().getId(), account.getCurrency());
    }
}
