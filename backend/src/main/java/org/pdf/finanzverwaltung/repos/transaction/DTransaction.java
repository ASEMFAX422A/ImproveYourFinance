package org.pdf.finanzverwaltung.repos.transaction;

import java.util.Date;

import org.pdf.finanzverwaltung.repos.bank.DBankStatement;

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
    private double amount;

    @ManyToOne
    @JoinColumn(name = "bank_statement_id", nullable = false)
    private DBankStatement bankStatement;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private DTransactionCategory category;

    public DTransaction() {
    }

    public DTransaction(Date date, double amount, DBankStatement bankStatement, DTransactionCategory category) {
        this.date = date;
        this.amount = amount;
        this.bankStatement = bankStatement;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
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
}
