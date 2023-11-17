package org.pdf.finanzverwaltung.dto;

import java.sql.Date;
import java.util.Set;

public class BankStatement {
    private long id;
    private BankAccount bankAccount;
    private Date issuedDate;
    private int oldBalance;
    private int newBalance;
    private String filePath;
    private Set<Transaction> transactions;

    public BankStatement() {
    }

    public BankStatement(BankAccount bankAccount, Date issueDate, int oldBalance, int newBalance) {
        this.bankAccount = bankAccount;
        this.issuedDate = issueDate;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
    }

    public long getId() {
        return id;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getOldBalance() {
        return oldBalance;
    }

    public int getNewBalance() {
        return newBalance;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
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

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

}
