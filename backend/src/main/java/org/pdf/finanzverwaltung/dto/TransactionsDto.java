package org.pdf.finanzverwaltung.dto;

import java.util.List;
import java.util.Set;

public class TransactionsDto {
    public String id;
    public double income;
    public double expenses;
    public Set<TransactionDTO> transactions;
    public List<DailyExpensesDTO> dailyExpenses;
    public List<CategoryExpensesDTO> categoryExpenses;
}
