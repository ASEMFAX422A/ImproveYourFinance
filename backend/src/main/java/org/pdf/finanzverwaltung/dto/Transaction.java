package org.pdf.finanzverwaltung.dto;

import java.util.Date;

public class Transaction {

    private long id;
    private Date date;
    private String title;
    private String desc;
    private double amount;
    private TransactionCategory category;

    public Transaction() {
    }

    public Transaction(long id, Date date, String title, String desc, double amount, TransactionCategory category) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.desc = desc;
        this.amount = amount;
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

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }
}
