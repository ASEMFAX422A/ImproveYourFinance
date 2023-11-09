package org.pdf.finanzverwaltung.repos.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * DTransactionCategory
 */
@Entity
public class DTransactionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    public DTransactionCategory() {
    }

    public DTransactionCategory(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
