package org.pdf.finanzverwaltung.repos.bank;

import java.util.Set;

import org.pdf.finanzverwaltung.repos.currency.DCurrency;
import org.pdf.finanzverwaltung.repos.user.DUser;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "iban")
public class DBankAccount {

    @Id
    private String iban;

    @Column(nullable = false)
    private String bic;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
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
        this.iban = iban;
        this.bic = bic;
        this.user = user;
        this.currency = currency;
    }

    public String getIban() {
        return iban;
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
