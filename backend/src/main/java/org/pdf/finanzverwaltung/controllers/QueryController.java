package org.pdf.finanzverwaltung.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.pdf.finanzverwaltung.dto.BankAccount;
import org.pdf.finanzverwaltung.dto.TransactionCategory;
import org.pdf.finanzverwaltung.models.BankAccountQuery;
import org.pdf.finanzverwaltung.repos.bank.BankAccountRepo;
import org.pdf.finanzverwaltung.repos.bank.DBankAccount;
import org.pdf.finanzverwaltung.repos.bank.DBankStatement;
import org.pdf.finanzverwaltung.repos.transaction.DTransactionCategory;
import org.pdf.finanzverwaltung.repos.transaction.TransactionCategoryRepo;
import org.pdf.finanzverwaltung.repos.user.DUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/query")
public class QueryController {
    private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

    @Autowired
    private BankAccountRepo bankAccountRepo;
    @Autowired
    private TransactionCategoryRepo transactionCategoryRepo;

    @PostMapping("/transaction-categorys")
    public ResponseEntity<Set<TransactionCategory>> queryTransactionsCategorys(Authentication authentication) {
        DUser user = (DUser) authentication.getPrincipal();

        List<DTransactionCategory> dCategories = transactionCategoryRepo.findAllByUser(user);
        Set<TransactionCategory> categories = new HashSet<>();
        for (DTransactionCategory category : dCategories) {
            categories.add(TransactionCategory.create(category));
        }

        return ResponseEntity.ok(categories);
    }

    @PostMapping("/bank-statements")
    public ResponseEntity<DBankStatement> queryBankStatement() {

        return null;
    }

    @PostMapping("/bank-accounts")
    public ResponseEntity<Set<BankAccount>> queryBankAccounts(Authentication authentication) {
        DUser user = (DUser) authentication.getPrincipal();

        List<DBankAccount> dBankAccounts = bankAccountRepo.findAllByUser(user);
        Set<BankAccount> bankAccounts = new HashSet<>();
        for (DBankAccount bankAccount : dBankAccounts) {
            bankAccounts.add(BankAccount.create(bankAccount));
        }

        return ResponseEntity.ok(bankAccounts);
    }

    @PostMapping("/bank-account")
    public ResponseEntity<BankAccount> queryBankAccount(Authentication authentication,
            @RequestBody BankAccountQuery query) {

        Optional<DBankAccount> bankAccountOpt = bankAccountRepo.findById(query.getIban());
        DUser user = (DUser) authentication.getPrincipal();

        if (!bankAccountOpt.isPresent())
            return ResponseEntity.badRequest().build();

        DBankAccount bankAccount = bankAccountOpt.get();
        if (!bankAccount.getUser().equals(user))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(BankAccount.create(bankAccount));
    }
}
