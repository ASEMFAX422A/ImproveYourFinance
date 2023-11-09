package org.pdf.finanzverwaltung.controllers;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.pdf.finanzverwaltung.repos.bank.BankAccountRepo;
import org.pdf.finanzverwaltung.repos.bank.BankStatementRepo;
import org.pdf.finanzverwaltung.repos.bank.DBankAccount;
import org.pdf.finanzverwaltung.repos.bank.DBankStatement;
import org.pdf.finanzverwaltung.repos.currency.CurrencyRepo;
import org.pdf.finanzverwaltung.repos.currency.DCurrency;
import org.pdf.finanzverwaltung.repos.transaction.DTransaction;
import org.pdf.finanzverwaltung.repos.transaction.DTransactionCategory;
import org.pdf.finanzverwaltung.repos.transaction.TransactionCategoryRepo;
import org.pdf.finanzverwaltung.repos.transaction.TransactionRepo;
import org.pdf.finanzverwaltung.repos.user.DUser;
import org.pdf.finanzverwaltung.repos.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/upload/account-statement")
public class UploadController {

    @Autowired
    private BankAccountRepo bankAccountRepo;

    @Autowired
    private CurrencyRepo currencyRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TransactionCategoryRepo transactionCategoryRepo;

    @Autowired
    private BankStatementRepo bankStatementRepo;

    public UploadController() {
    }

    @PostMapping
    public ResponseEntity<String> accountStatement(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<DUser> ouser = userRepo.findByUsername(userDetails.getUsername());
        if (!ouser.isPresent())
            return ResponseEntity.badRequest().body("NoNo");

        DUser user = ouser.get();

        Optional<DCurrency> oCur = currencyRepo.findByShortName("€");
        DCurrency currency;
        if (!oCur.isPresent()) {
            currency = new DCurrency("€", "Euro");
            currencyRepo.save(currency);
        } else {
            currency = oCur.get();
        }

        Optional<DTransactionCategory> oTransCat = transactionCategoryRepo.findByName("Einkauf");
        DTransactionCategory transCat;
        if (!oTransCat.isPresent()) {
            transCat = new DTransactionCategory("Einkauf");
            transactionCategoryRepo.save(transCat);
        } else {
            transCat = oTransCat.get();
        }

        DBankAccount bankAccount = new DBankAccount("IBAN: " + new Random().nextInt(), user, currency);
        bankAccountRepo.save(bankAccount);

        final Random ran = new Random();

        for (int i = 0; i < ran.nextInt(4) + 1; i++) {
            DBankStatement statement = new DBankStatement(bankAccount, new Date(), 0, 10);
            bankStatementRepo.save(statement);

            for (int j = 0; j < ran.nextInt(4) + 1; j++) {
                DTransaction transaction = new DTransaction(new Date(), ran.nextInt(1673), statement, transCat,
                        currency);
                transactionRepo.save(transaction);
            }

        }
        return ResponseEntity.ok("Ok");
    }

    @GetMapping
    public ResponseEntity<String> accountsCount(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<DUser> ouser = userRepo.findByUsername(userDetails.getUsername());
        if (!ouser.isPresent())
            return ResponseEntity.badRequest().body("NoNo");

        DUser user = ouser.get();
        final String NEW_LINE = "\n";
        StringBuilder response = new StringBuilder("User: " + user.getUsername() + NEW_LINE);

        for (DBankAccount acc : user.getBankAccounts()) {
            response.append("BankAccount: " + acc.getIban() + "(" + acc.getCurrency().getShortName() + ")" + NEW_LINE);
            for (DBankStatement statement : acc.getStatements()) {
                response.append(
                        "\tStatement from: " + statement.getIssuedDate() + "(" + statement.getOldBalance()
                                + "->" + statement.getNewBalance() + ")" + NEW_LINE);
                for (DTransaction transaction : statement.getTransactions()) {
                    response.append(
                            "\t\tTransaction: " + transaction.getAmount() + transaction.getCurrency().getShortName()
                                    + " - " + transaction.getCategory().getName() + NEW_LINE);
                }
            }
        }

        return ResponseEntity.ok(response.toString());
    }

}
