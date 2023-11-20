package org.pdf.finanzverwaltung.dto;

import java.util.Set;

public class BankAccountOverviewDto {
    public String id;
    public double endBalance;
    public double startBalance;
    public Set<Transaction> transactions;
}
