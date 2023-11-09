package org.pdf.finanzverwaltung.repos.bank;

import java.util.Set;

import org.pdf.finanzverwaltung.repos.currency.DCurrency;
import org.pdf.finanzverwaltung.repos.user.DUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class DBankAccount {

    @Id
    private String iban;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DUser user;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private DCurrency currency;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DBankStatement> statements;

    public DBankAccount() {
    }

    public DBankAccount(String iban, DUser user, DCurrency currency) {
        this.iban = iban;
        this.user = user;
        this.currency = currency;
    }

    public String getIban() {
        return iban;
    }

    public DUser getUser() {
        return user;
    }

    public DCurrency getCurrency() {
        return currency;
    }

    public Set<DBankStatement> getStatements() {
        return statements;
    }
}
