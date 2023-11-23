package org.pdf.finanzverwaltung.dto;

public class CurrencyDTO {
    public long id;
    public String shortName;
    public String longName;

    public CurrencyDTO() {
    }

    public CurrencyDTO(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }
}
