package org.pdf.finanzverwaltung.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.pdf.finanzverwaltung.dto.BankAccount;
import org.pdf.finanzverwaltung.dto.User;
import org.pdf.finanzverwaltung.models.DBankAccount;
import org.pdf.finanzverwaltung.models.DUser;
import org.pdf.finanzverwaltung.repos.bank.BankAccountRepo;
import org.pdf.finanzverwaltung.repos.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountRepo bankAccountRepo;

    @Autowired
    private CurrencyService currencyService;

    public BankAccountService() {
    }

    public boolean currentUserHasAccount(String id) {
        return bankAccountRepo.existsByIdAndUserId(id, userService.getCurrentDUser());
    }

    public Set<BankAccount> getAllForCurrentUser() {
        List<DBankAccount> dBankAccounts = bankAccountRepo.findAllByUser(userService.getCurrentDUser());

        Set<BankAccount> bankAccounts = new HashSet<>();
        for (DBankAccount bankAccount : dBankAccounts) {
            bankAccounts.add(dBankAccountToBankAccount(bankAccount));
        }
        return bankAccounts;
    }

    public Set<BankAccount> getAllForUser(User user) {
        List<DBankAccount> dBankAccounts = bankAccountRepo.findAllByUser(userService.userToDUser(user));

        Set<BankAccount> bankAccounts = new HashSet<>();
        for (DBankAccount bankAccount : dBankAccounts) {
            bankAccounts.add(dBankAccountToBankAccount(bankAccount));
        }
        return bankAccounts;
    }

    public BankAccount getByIdAndCurrentUser(String iban) {
        final DBankAccount bankAccount = bankAccountRepo.findByIdAndUser(iban, userService.getCurrentDUser());

        return dBankAccountToBankAccount(bankAccount);
    }

    public BankAccount getByIdAndUser(String iban, User user) {
        final DBankAccount bankAccount = bankAccountRepo.findByIdAndUser(iban, userService.userToDUser(user));

        return dBankAccountToBankAccount(bankAccount);
    }

    public BankAccount dBankAccountToBankAccount(DBankAccount account) {
        if (account == null)
            return null;

        return new BankAccount(account.getIban(), account.getBic(), account.getUser().getId(),
                currencyService.dCurrencyToCurrency(account.getCurrency()));
    }

    public DBankAccount bankAccountToDBankAccount(BankAccount account) {
        if (account == null)
            return null;

        Optional<DBankAccount> bankAccountOpt = bankAccountRepo.findById(account.getIban());
        if (bankAccountOpt.isPresent())
            return bankAccountOpt.get();

        final Optional<DUser> user = userRepo.findById(account.getOwner());

        return new DBankAccount(account.getIban(), account.getBic(), user.get(),
                currencyService.currencyToDCurrency(account.getCurrency()));
    }
}
