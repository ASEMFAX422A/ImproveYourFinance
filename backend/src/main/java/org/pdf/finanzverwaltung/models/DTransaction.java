package org.pdf.finanzverwaltung.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class DTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private String title;

    @Column
    private String desc;

    @Column(nullable = false)
    private double amount;

    @ManyToOne
    @JoinColumn(name = "bank_statement_id", nullable = false)
    private DBankStatement bankStatement;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private DTransactionCategory category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DUser user;

    public DTransaction() {
    }

    public DTransaction(Date date, String title, String desc, double amount, DBankStatement bankStatement,
            DTransactionCategory category, DUser user) {
        this.date = date;
        this.title = title;
        this.desc = desc;
        this.amount = amount;
        this.bankStatement = bankStatement;
        this.category = category;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return desc;
    }

    public double getAmount() {
        return amount;
    }

    public DBankStatement getBankStatement() {
        return bankStatement;
    }

    public DTransactionCategory getCategory() {
        return category;
    }

    public DUser getUser() {
        return user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBankStatement(DBankStatement bankStatement) {
        this.bankStatement = bankStatement;
    }

    public void setCategory(DTransactionCategory category) {
        this.category = category;
    }

    public void setUser(DUser user) {
        this.user = user;
    }
}
