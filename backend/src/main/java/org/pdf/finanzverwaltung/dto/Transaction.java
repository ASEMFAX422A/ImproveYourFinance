package org.pdf.finanzverwaltung.dto;

import java.util.Date;

public class Transaction {

    private long id;
    private String title;
    private Date date;
    private double amount;
    private String desc;
    private BankStatement bankStatement;
    private TransactionCategory category;

    public Transaction() {
    }

    public Transaction(Date date, double amount, BankStatement bankStatement, TransactionCategory category,
            Currency currency) {
        this.date = date;
        this.amount = amount;
        this.bankStatement = bankStatement;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getDesc() {
        return desc;
    }

    public BankStatement getBankStatement() {
        return bankStatement;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public void setBankStatement(BankStatement bankStatement) {
        this.bankStatement = bankStatement;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }
}
