package org.pdf.finanzverwaltung.dto;

import java.sql.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankStatement {
    private long id;

    private BankAccount bankAccount;

    private Date issuedDate;

    private int oldBalance;

    private String filePath;

    private Set<Transaction> transactions;

    public BankStatement() {
    }

    public BankStatement(BankAccount bankAccount, Date issueDate, int oldBalance) {
        this.bankAccount = bankAccount;
        this.issuedDate = issueDate;
        this.oldBalance = oldBalance;
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

    @JsonProperty("newBalance")
    public int getNewBalance() {
        if (transactions == null)
            return 0;
        return transactions.stream().mapToInt(Transaction::getAmount).sum();
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }
}
