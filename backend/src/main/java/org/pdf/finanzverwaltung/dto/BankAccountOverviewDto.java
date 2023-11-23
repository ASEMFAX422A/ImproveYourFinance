package org.pdf.finanzverwaltung.dto;

import java.util.List;
import java.util.Set;

public class BankAccountOverviewDto {
    public String id;
    public double endBalance;
    public double startBalance;
    public Set<TransactionDTO> transactions;
    public List<DailyExpensesDTO> dailyExpenses;
    public List<CategoryExpensesDTO> categoryExpenses;
}
