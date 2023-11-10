package org.pdf.finanzverwaltung.repos.currency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * DCurrency
 */
@Entity
public class DCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false, length = 10)
    private String shortName;

    @Column(unique = true, length = 50)
    private String longName;

    public DCurrency() {
    }

    public DCurrency(String shortName, String longName) {
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
