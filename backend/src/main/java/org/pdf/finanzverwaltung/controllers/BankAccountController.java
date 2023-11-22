package org.pdf.finanzverwaltung.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.pdf.finanzverwaltung.dto.BankAccountDTO;
import org.pdf.finanzverwaltung.dto.BankAccountOverviewDto;
import org.pdf.finanzverwaltung.dto.BankAccountOverviewQuery;
import org.pdf.finanzverwaltung.dto.BankAccountQuery;
import org.pdf.finanzverwaltung.dto.BankStatementDTO;
import org.pdf.finanzverwaltung.dto.CategoryExpensesDTO;
import org.pdf.finanzverwaltung.dto.DailyExpensesDTO;
import org.pdf.finanzverwaltung.dto.TransactionDTO;
import org.pdf.finanzverwaltung.services.BankAccountService;
import org.pdf.finanzverwaltung.services.BankStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bank-account")
public class BankAccountController {

    @Autowired
    private BankStatementService bankStatementService;

    @Autowired
    private BankAccountService bankAccountService;

    public BankAccountController() {
    }

    @PostMapping("/query-accounts")
    public ResponseEntity<Set<BankAccountDTO>> queryBankAccounts() {
        final Set<BankAccountDTO> bankAccounts = bankAccountService.getAllForCurrentUser();

        return ResponseEntity.ok(bankAccounts);
    }

    @PostMapping("/query-account")
    public ResponseEntity<BankAccountDTO> queryBankAccount(@RequestBody BankAccountQuery query) {
        final BankAccountDTO bankAccount = bankAccountService.getByIdAndCurrentUser(query.iban);

        if (bankAccount == null)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/overview")
    public ResponseEntity<BankAccountOverviewDto> queryMonth(@RequestBody BankAccountOverviewQuery query) {
        // TODO Accounts with different currencies ???
        final BankAccountOverviewDto response = new BankAccountOverviewDto();
        response.id = query.id;
        response.transactions = new HashSet<>();
        response.dailyExpenses = new ArrayList<>();
        response.categoryExpenses = new ArrayList<>();

        if (query.id.equalsIgnoreCase("all")) {
            final Set<BankStatementDTO> statement = bankStatementService.getByCurrentUserAndIssuedDateBetween(
                    new Date(query.start),
                    new Date(query.end));
            if (statement == null)
                return ResponseEntity.badRequest().body(null);

            for (BankStatementDTO bankStatement : statement) {
                response.startBalance += bankStatement.getOldBalance();
                response.endBalance += bankStatement.getNewBalance();
                response.transactions.addAll(bankStatement.getTransactions());
            }
        } else {
            final BankStatementDTO statement = bankStatementService.getByUserAndBankAccountAndIssuedDateBetween(
                    query.id,
                    new Date(query.start),
                    new Date(query.end));
            if (statement == null)
                return ResponseEntity.badRequest().body(null);

            response.startBalance += statement.getOldBalance();
            response.endBalance += statement.getNewBalance();
            response.transactions.addAll(statement.getTransactions());
        }

        CategoryExpensesDTO noCategory = null;
        for (TransactionDTO trans : response.transactions) {

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
                dailyExpenses.date = new Date(trans.date.getTime());
                response.dailyExpenses.add(dailyExpenses);
            }
            dailyExpenses.amount += trans.amount;

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

        response.dailyExpenses.sort((o1, o2) -> o1.date.compareTo(o2.date));
        return ResponseEntity.ok(response);
    }
}
