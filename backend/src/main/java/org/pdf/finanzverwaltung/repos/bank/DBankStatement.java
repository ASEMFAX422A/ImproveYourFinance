package org.pdf.finanzverwaltung.repos.bank;

import java.util.Date;
import java.util.Set;

import org.pdf.finanzverwaltung.repos.transaction.DTransaction;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * DBankStatement
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DBankStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", nullable = false)
    private DBankAccount bankAccount;

    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    private Date issuedDate;

    @Column(nullable = false)
    private double oldBalance;

    @Column(nullable = false)
    private double newBalance;

    @Column(nullable = false)
    private String filePath;

    @OneToMany(mappedBy = "bankStatement", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DTransaction> transactions;

    public DBankStatement() {
    }

    public DBankStatement(DBankAccount bankAccount, Date issueDate, double oldBalance, double newBalance,
            String filePath) {
        this.bankAccount = bankAccount;
        this.issuedDate = issueDate;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.filePath = filePath;
    }

    public long getId() {
        return id;
    }

    public DBankAccount getBankAccount() {
        return bankAccount;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public double getOldBalance() {
        return oldBalance;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public Set<DTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<DTransaction> transactions) {
        this.transactions = transactions;
    }
}
