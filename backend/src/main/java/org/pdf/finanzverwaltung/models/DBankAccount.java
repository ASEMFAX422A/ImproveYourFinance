package org.pdf.finanzverwaltung.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class DBankAccount {

    @Id
    private String id;

    @Column(nullable = false)
    private String bic;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DUser user;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private DCurrency currency;

    @JsonManagedReference
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DBankStatement> statements;

    public DBankAccount() {
    }

    public DBankAccount(String iban, String bic, DUser user, DCurrency currency) {
        this.id = iban;
        this.bic = bic;
        this.user = user;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public String getIban() {
        return id;
    }

    public String getBic() {
        return bic;
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
