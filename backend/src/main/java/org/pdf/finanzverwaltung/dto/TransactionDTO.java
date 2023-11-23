package org.pdf.finanzverwaltung.dto;

import java.util.Date;

public class TransactionDTO {
    public long id;
    public Date date;
    public String title;
    public String desc;
    public double amount;
    public TransactionCategory category;

    public TransactionDTO() {
    }

    public TransactionDTO(long id, Date date, String title, String desc, double amount, TransactionCategory category) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.desc = desc;
        this.amount = amount;
        this.category = category;
    }
}
