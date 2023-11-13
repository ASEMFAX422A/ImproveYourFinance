package org.pdf.finanzverwaltung.dto;

import org.pdf.finanzverwaltung.repos.transaction.DTransactionCategory;

public class TransactionCategory {
    private String name;
    private long id;

    public TransactionCategory(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TransactionCategory create(DTransactionCategory category) {
        return new TransactionCategory(category.getId(), category.getName());
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
