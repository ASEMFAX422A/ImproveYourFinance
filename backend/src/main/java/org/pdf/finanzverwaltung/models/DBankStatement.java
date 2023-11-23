package org.pdf.finanzverwaltung.models;

import java.util.Date;
import java.util.Set;

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

@Entity
public class DBankStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", nullable = false)
    private DBankAccount bankAccount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DUser user;

    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    private Date issuedDate;

    @Column(nullable = false)
    private double oldBalance;

    @Column(nullable = false)
    private double newBalance;

    @Column(nullable = false)
    private String fileName;

    @OneToMany(mappedBy = "bankStatement", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DTransaction> transactions;

    public DBankStatement() {
    }

    public DBankStatement(DBankAccount bankAccount, DUser user, Date issueDate, double oldBalance, double newBalance,
            String fileName) {
        this.bankAccount = bankAccount;
        this.user = user;
        this.issuedDate = issueDate;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.fileName = fileName;
    }

    public long getId() {
        return id;
    }

    public DBankAccount getBankAccount() {
        return bankAccount;
    }

    public DUser getUser() {
        return user;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public String getFileName() {
        return fileName;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setBankAccount(DBankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setUser(DUser user) {
        this.user = user;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public void setOldBalance(double oldBalance) {
        this.oldBalance = oldBalance;
    }

    public void setNewBalance(double newBalance) {
        this.newBalance = newBalance;
    }

    public void setFileName(String filePath) {
        this.fileName = filePath;
    }

    public void setTransactions(Set<DTransaction> transactions) {
        this.transactions = transactions;
    }
}
