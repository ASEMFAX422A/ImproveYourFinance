package org.pdf.finanzverwaltung.dto;

public class TransactionCategory {
    private long id;
    private String name;
    private String matcherPattern;

    public TransactionCategory(long id, String name, String matcherPattern) {
        this.id = id;
        this.name = name;
        this.matcherPattern = matcherPattern;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMatcherPattern() {
        return matcherPattern;
    }
}
