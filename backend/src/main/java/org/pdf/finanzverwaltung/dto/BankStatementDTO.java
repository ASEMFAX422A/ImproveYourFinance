package org.pdf.finanzverwaltung.dto;

import java.util.Date;
import java.util.Set;

public class BankStatementDTO {
    private long id;
    private Date issuedDate;
    private double oldBalance;
    private double newBalance;
    private Set<TransactionDTO> transactions;

    public BankStatementDTO() {
    }

    public BankStatementDTO(long id, Date issueDate, double oldBalance, double newBalance,
            Set<TransactionDTO> transactions) {
        this.id = id;
        this.issuedDate = issueDate;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.transactions = transactions;
    }

    public long getId() {
        return id;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public double getOldBalance() {
        return oldBalance;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public void setOldBalance(int oldBalance) {
        this.oldBalance = oldBalance;
    }

    public void setNewBalance(int newBalance) {
        this.newBalance = newBalance;
    }

    public void setTransactions(Set<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
