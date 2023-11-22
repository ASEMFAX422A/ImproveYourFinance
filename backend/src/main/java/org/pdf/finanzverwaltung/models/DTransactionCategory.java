package org.pdf.finanzverwaltung.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DTransactionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DUser user;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(nullable = true)
    private String matcherPattern;

    @Column
    private boolean matchDescription;

    public DTransactionCategory() {
    }

    public DTransactionCategory(DUser user, String name, String matcherPattern, boolean matchDescription) {
        this.user = user;
        this.name = name;
        this.matcherPattern = matcherPattern;
        this.matchDescription = matchDescription;
    }

    public long getId() {
        return id;
    }

    public DUser getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getMatcherPattern() {
        return matcherPattern;
    }

    public boolean isMatchDescription() {
        return matchDescription;
    }
}
