package org.pdf.finanzverwaltung.dto;

public class Currency {
    private long id;
    private String shortName;
    private String longName;

    public Currency() {
    }

    public Currency(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public long getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }
}
