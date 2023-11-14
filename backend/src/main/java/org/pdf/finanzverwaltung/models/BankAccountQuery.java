package org.pdf.finanzverwaltung.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankAccountQuery {
    private final String iban;

    public BankAccountQuery(@JsonProperty("iban") String iban) {
        this.iban = iban;
    }

    public String getIban() {
        return iban;
    }
}
