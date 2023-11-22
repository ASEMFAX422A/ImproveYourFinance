package org.pdf.finanzverwaltung.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.pdf.finanzverwaltung.dto.BankAccount;
import org.pdf.finanzverwaltung.dto.BankAccountOverviewDto;
import org.pdf.finanzverwaltung.dto.BankAccountOverviewQuery;
import org.pdf.finanzverwaltung.dto.BankAccountQuery;
import org.pdf.finanzverwaltung.dto.BankStatement;
import org.pdf.finanzverwaltung.dto.CategoryExpenses;
import org.pdf.finanzverwaltung.dto.DailyExpenses;
import org.pdf.finanzverwaltung.dto.Transaction;
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
    public ResponseEntity<Set<BankAccount>> queryBankAccounts() {
        final Set<BankAccount> bankAccounts = bankAccountService.getAllForCurrentUser();

        return ResponseEntity.ok(bankAccounts);
    }

    @PostMapping("/query-account")
    public ResponseEntity<BankAccount> queryBankAccount(@RequestBody BankAccountQuery query) {
        final BankAccount bankAccount = bankAccountService.getByIdAndCurrentUser(query.iban);

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
            final Set<BankStatement> statement = bankStatementService.getByCurrentUserAndIssuedDateBetween(
                    new Date(query.start),
                    new Date(query.end));
            if (statement == null)
                return ResponseEntity.badRequest().body(null);

            for (BankStatement bankStatement : statement) {
                response.startBalance += bankStatement.getOldBalance();
                response.endBalance += bankStatement.getNewBalance();
                response.transactions.addAll(bankStatement.getTransactions());
            }
        } else {
            final BankStatement statement = bankStatementService.getByUserAndBankAccountAndIssuedDateBetween(query.id,
                    new Date(query.start),
                    new Date(query.end));
            if (statement == null)
                return ResponseEntity.badRequest().body(null);

            response.startBalance += statement.getOldBalance();
            response.endBalance += statement.getNewBalance();
            response.transactions.addAll(statement.getTransactions());
        }

        CategoryExpenses noCategory = null;
        for (Transaction trans : response.transactions) {

            /* ==== Creating Daily Expenses ==== */
            DailyExpenses dailyExpenses = null;
            for (DailyExpenses dailyExp : response.dailyExpenses) {
                if (dailyExp.date.compareTo(trans.getDate()) == 0) {
                    dailyExpenses = dailyExp;
                    break;
                }
            }

            if (dailyExpenses == null) {
                dailyExpenses = new DailyExpenses();
                dailyExpenses.date = trans.getDate();
                response.dailyExpenses.add(dailyExpenses);
            }
            dailyExpenses.amount += trans.getAmount();

            /* ==== Creating Category Expenses ==== */
            if (trans.getCategory() == null) {
                if (noCategory == null) {
                    noCategory = new CategoryExpenses();
                    response.categoryExpenses.add(noCategory);
                }

                noCategory.amount += trans.getAmount();
                continue;
            }

            CategoryExpenses categoryExpenses = null;
            for (CategoryExpenses categoryExp : response.categoryExpenses) {
                if (categoryExp.category == null) {
                    continue;
                }

                if (categoryExp.category.getName().equals(trans.getCategory().getName())) {
                    categoryExpenses = categoryExp;
                    break;
                }
            }

            if (categoryExpenses == null) {
                categoryExpenses = new CategoryExpenses();
                categoryExpenses.category = trans.getCategory();
                response.categoryExpenses.add(categoryExpenses);
            }
            categoryExpenses.amount += trans.getAmount();
        }

        response.dailyExpenses.sort((o1, o2) -> (int) (o1.date.getTime() - o2.date.getTime()));
        return ResponseEntity.ok(response);
    }
}
