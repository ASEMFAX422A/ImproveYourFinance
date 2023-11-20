package org.pdf.finanzverwaltung.controllers;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.pdf.finanzverwaltung.dto.BankAccount;
import org.pdf.finanzverwaltung.dto.BankAccountOverviewDto;
import org.pdf.finanzverwaltung.dto.BankAccountOverviewQuery;
import org.pdf.finanzverwaltung.dto.BankAccountQuery;
import org.pdf.finanzverwaltung.dto.BankStatement;
import org.pdf.finanzverwaltung.dto.TransactionCategory;
import org.pdf.finanzverwaltung.dto.User;
import org.pdf.finanzverwaltung.services.BankAccountService;
import org.pdf.finanzverwaltung.services.BankStatementService;
import org.pdf.finanzverwaltung.services.TransactionService;
import org.pdf.finanzverwaltung.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/query")
public class QueryController {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankStatementService bankStatementService;

    @PostMapping("/transaction-categorys")
    public ResponseEntity<Set<TransactionCategory>> queryTransactionsCategorys() {
        User user = userService.getCurrentUser();
        Set<TransactionCategory> categories = transactionService.getAllCategoriesForUser(user);

        return ResponseEntity.ok(categories);
    }

    @PostMapping("/bank-statements")
    public ResponseEntity<Set<BankStatement>> queryBankStatement(@RequestBody BankAccountQuery query) {
        User user = userService.getCurrentUser();
        Set<BankStatement> bankAccounts = bankStatementService.getAllByIbanAndUser(query.getIban(), user);

        return ResponseEntity.ok(bankAccounts);
    }

    @PostMapping("/bank-accounts")
    public ResponseEntity<Set<BankAccount>> queryBankAccounts() {
        User user = userService.getCurrentUser();
        Set<BankAccount> bankAccounts = bankAccountService.getAllForUser(user);

        return ResponseEntity.ok(bankAccounts);
    }

    @PostMapping("/bank-account")
    public ResponseEntity<BankAccount> queryBankAccount(@RequestBody BankAccountQuery query) {
        Optional<BankAccount> bankAccountOpt = bankAccountService.getByIdAndUser(query.getIban(),
                userService.getCurrentUser());

        if (!bankAccountOpt.isPresent())
            return ResponseEntity.badRequest().build();

        BankAccount bankAccount = bankAccountOpt.get();
        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/overview-bank-account")
    public ResponseEntity<BankAccountOverviewDto> queryMonth(@RequestBody BankAccountOverviewQuery query) {
        final BankAccountOverviewDto response = new BankAccountOverviewDto();
        final boolean all = query.id.equalsIgnoreCase("all");

        // Accounts with different currencies ???
        if (all) {
            response.transactions = transactionService.getAllBetweenForCurrentUser(new Date(query.start),
                    new Date(query.end));
        } else {
            Optional<BankAccount> bankAccountOpt = bankAccountService.getByIdAndCurrentUser(query.id);
            if (bankAccountOpt.isEmpty())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

            response.transactions = transactionService.getByIdBetweenForCurrentUser(query.id, new Date(query.start),
                    new Date(query.end));
        }

        response.id = query.id;
        response.startBalance = 10;
        return ResponseEntity.ok(response);
    }
}
