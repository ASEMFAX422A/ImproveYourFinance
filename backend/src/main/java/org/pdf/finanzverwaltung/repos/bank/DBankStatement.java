package org.pdf.finanzverwaltung.repos.bank;

import java.util.Date;
import java.util.Set;

import org.pdf.finanzverwaltung.repos.transaction.DTransaction;

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
    private int oldBalance;

    @Column(nullable = false)
    private int newBalance;

    @OneToMany(mappedBy = "bankStatement", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DTransaction> transactions;

    public DBankStatement() {
    }

    public DBankStatement(DBankAccount bankAccount, Date issueDate, int oldBalance, int newBalance) {
        this.bankAccount = bankAccount;
        this.issuedDate = issueDate;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
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

    public int getOldBalance() {
        return oldBalance;
    }

    public int getNewBalance() {
        return newBalance;
    }

    public Set<DTransaction> getTransactions() {
        return transactions;
    }
}
