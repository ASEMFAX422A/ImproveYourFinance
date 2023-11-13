package org.pdf.finanzverwaltung.dto;

import java.sql.Date;

public class Transaction {

    private long id;

    private Date date;

    private int amount;

    private BankStatement bankStatement;

    private TransactionCategory category;

    private DCurrency currency;

    public Transaction() {
    }

    public Transaction(Date date, int amount, BankStatement bankStatement, TransactionCategory category,
            DCurrency currency) {
        this.date = date;
        this.amount = amount;
        this.bankStatement = bankStatement;
        this.category = category;
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public BankStatement getBankStatement() {
        return bankStatement;
    }

    public Currency getCurrency() {
        return currency;
    }

    public TransactionCategory getCategory() {
        return category;
    }

}
