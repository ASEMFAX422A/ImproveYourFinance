package org.pdf.finanzverwaltung.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.pdf.finanzverwaltung.dto.BankAccountOverviewQuery;
import org.pdf.finanzverwaltung.dto.CategoryExpensesDTO;
import org.pdf.finanzverwaltung.dto.DailyExpensesDTO;
import org.pdf.finanzverwaltung.dto.EditCategoryQuery;
import org.pdf.finanzverwaltung.dto.MessageDto;
import org.pdf.finanzverwaltung.dto.TransactionCategory;
import org.pdf.finanzverwaltung.dto.TransactionDTO;
import org.pdf.finanzverwaltung.dto.TransactionsDto;
import org.pdf.finanzverwaltung.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/categorys")
    public ResponseEntity<Set<TransactionCategory>> queryTransactionsCategorys() {
        final Set<TransactionCategory> categories = transactionService.getAllCategoriesForCurrentUser();

        return ResponseEntity.ok(categories);
    }

    @PostMapping("/edit-category")
    public ResponseEntity<MessageDto> editOrCreateCategory(@RequestBody EditCategoryQuery query) {
        final boolean created = transactionService.createOrEditCategoryForCurrentUser(query);
        if (created)
            return MessageDto.createResponse(HttpStatus.OK, "Category edited");

        return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Category already exists");
    }

    @PostMapping("/remove-category")
    public ResponseEntity<MessageDto> removeCategory(@RequestParam long id) {
        final boolean created = transactionService.removeCategoryForCurrentUserByName(id);
        if (created)
            return MessageDto.createResponse(HttpStatus.OK, "Category removed");

        return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Something went wrong");
    }

    @PostMapping("/query-transactions")
    public ResponseEntity<TransactionsDto> queryTransactions(@RequestBody BankAccountOverviewQuery query) {
        final TransactionsDto response = new TransactionsDto();
        response.id = query.id;
        response.dailyIncome = new ArrayList<>();
        response.dailyExpenses = new ArrayList<>();
        response.dailyFinances = new ArrayList<>();
        response.categoryExpenses = new ArrayList<>();

        if (query.id.equalsIgnoreCase("all")) {
            response.transactions = transactionService.getAllBetweenForCurrentUser(new Date(query.start),
                    new Date(query.end));
        } else {
            response.transactions = transactionService.getAllForBankAccountAndBetweenAndCurrentUser(query.id,
                    new Date(query.start),
                    new Date(query.end));
        }

        if (response.transactions == null) {
            // User does not own bank account or bank account does not exists
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        CategoryExpensesDTO noCategory = null;
        for (TransactionDTO trans : response.transactions) {
            if (trans.amount < 0) {
                response.expenses += trans.amount;
            } else
                response.income += trans.amount;

            /* ==== Creating Daily Expenses ==== */
            DailyExpensesDTO dailyExpenses = null;
            for (DailyExpensesDTO dailyExp : response.dailyExpenses) {
                if (dailyExp.date.compareTo(trans.date) == 0) {
                    dailyExpenses = dailyExp;
                    break;
                }
            }

            if (dailyExpenses == null) {
                dailyExpenses = new DailyExpensesDTO();
                dailyExpenses.date = trans.date;
                response.dailyExpenses.add(dailyExpenses);
            }

            /* ==== Creating Daily Income ==== */
            DailyExpensesDTO dailyIncome = null;
            for (DailyExpensesDTO dailyExp : response.dailyIncome) {
                if (dailyExp.date.compareTo(trans.date) == 0) {
                    dailyIncome = dailyExp;
                    break;
                }
            }

            if (dailyIncome == null) {
                dailyIncome = new DailyExpensesDTO();
                dailyIncome.date = trans.date;
                response.dailyIncome.add(dailyIncome);
            }

            if (trans.amount < 0)
                dailyExpenses.amount += trans.amount;
            else
                dailyIncome.amount += trans.amount;

            /* ==== Creating Daily Finances ==== */
            DailyExpensesDTO dailyFinances = null;
            for (DailyExpensesDTO dailyExp : response.dailyFinances) {
                if (dailyExp.date.compareTo(trans.date) == 0) {
                    dailyFinances = dailyExp;
                    break;
                }
            }

            if (dailyFinances == null) {
                dailyFinances = new DailyExpensesDTO();
                dailyFinances.date = trans.date;
                response.dailyFinances.add(dailyFinances);
            }
            dailyFinances.amount += trans.amount;

            /* ==== Creating Category Expenses ==== */
            if (trans.category == null) {
                if (noCategory == null) {
                    noCategory = new CategoryExpensesDTO();
                    response.categoryExpenses.add(noCategory);
                }

                noCategory.amount += trans.amount;
                continue;
            }

            CategoryExpensesDTO categoryExpenses = null;
            for (CategoryExpensesDTO categoryExp : response.categoryExpenses) {
                if (categoryExp.category == null) {
                    continue;
                }

                if (categoryExp.category.getName().equals(trans.category.getName())) {
                    categoryExpenses = categoryExp;
                    break;
                }
            }

            if (categoryExpenses == null) {
                categoryExpenses = new CategoryExpensesDTO();
                categoryExpenses.category = trans.category;
                response.categoryExpenses.add(categoryExpenses);
            }
            categoryExpenses.amount += trans.amount;
        }

        response.dailyIncome.sort((o1, o2) -> o1.date.compareTo(o2.date));
        response.dailyExpenses.sort((o1, o2) -> o1.date.compareTo(o2.date));
        response.dailyFinances.sort((o1, o2) -> o1.date.compareTo(o2.date));
        return ResponseEntity.ok(response);
    }
}
