package org.pdf.finanzverwaltung.dto;

import java.util.List;

public class TransactionsDto {
    public String id;
    public double income;
    public double expenses;
    public List<TransactionDTO> transactions;
    public List<DailyExpensesDTO> dailyExpenses;
    public List<CategoryExpensesDTO> categoryExpenses;
}
