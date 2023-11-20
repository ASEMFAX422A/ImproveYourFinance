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

    public Set<BankAccount> getAllForUser(User user) {
        List<DBankAccount> dBankAccounts = bankAccountRepo.findAllByUser(userService.userToDUser(user));

        Set<BankAccount> bankAccounts = new HashSet<>();
        for (DBankAccount bankAccount : dBankAccounts) {
            bankAccounts.add(dBankAccountToBankAccount(bankAccount));
        }
        return bankAccounts;
    }

    public Optional<BankAccount> getByIdAndCurrentUser(String iban) {
        Optional<DBankAccount> bankAccountOpt = bankAccountRepo.findByIdAndUser(iban, userService.getCurrentDUser());

        if (bankAccountOpt.isPresent()) {
            return Optional.of(dBankAccountToBankAccount(bankAccountOpt.get()));
        }

        return Optional.empty();
    }

    public Optional<BankAccount> getByIdAndUser(String iban, User user) {
        Optional<DBankAccount> bankAccountOpt = bankAccountRepo.findByIdAndUser(iban, userService.userToDUser(user));

        if (bankAccountOpt.isPresent()) {
            return Optional.of(dBankAccountToBankAccount(bankAccountOpt.get()));
        }

        return Optional.empty();
    }

    public BankAccount dBankAccountToBankAccount(DBankAccount account) {
        return new BankAccount(account.getIban(), account.getBic(), account.getUser().getId(),
                currencyService.dCurrencyToCurrency(account.getCurrency()));
    }

    public DBankAccount bankAccountToDBankAccount(BankAccount account) {
        Optional<DBankAccount> bankAccountOpt = bankAccountRepo.findById(account.getIban());
        if (bankAccountOpt.isPresent())
            return bankAccountOpt.get();

        final Optional<DUser> user = userRepo.findById(account.getOwner());

        return new DBankAccount(account.getIban(), account.getBic(), user.get(),
                currencyService.currencyToDCurrency(account.getCurrency()));
    }

    public boolean currentUserHasAccount(String id) {
        return bankAccountRepo.existsByIdAndUserId(id, userService.getCurrentDUser());
    }
}
