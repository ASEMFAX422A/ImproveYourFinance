package org.pdf.finanzverwaltung.repos.transaction;

import java.util.Date;

import org.pdf.finanzverwaltung.repos.bank.DBankStatement;
import org.pdf.finanzverwaltung.repos.currency.DCurrency;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * DTransaction
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private int amount;

    @ManyToOne
    @JoinColumn(name = "bank_statement_id", nullable = false)
    private DBankStatement bankStatement;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private DTransactionCategory category;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private DCurrency currency;

    public DTransaction() {
    }

    public DTransaction(Date date, int amount, DBankStatement bankStatement, DTransactionCategory category,
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

    public DBankStatement getBankStatement() {
        return bankStatement;
    }

    public DCurrency getCurrency() {
        return currency;
    }

    public DTransactionCategory getCategory() {
        return category;
    }
}
