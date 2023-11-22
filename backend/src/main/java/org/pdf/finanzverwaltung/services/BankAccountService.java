package org.pdf.finanzverwaltung.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.pdf.finanzverwaltung.dto.BankAccountDTO;
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

    public Set<BankAccountDTO> getAllForCurrentUser() {
        List<DBankAccount> dBankAccounts = bankAccountRepo.findAllByUser(userService.getCurrentDUser());

        Set<BankAccountDTO> bankAccounts = new HashSet<>();
        for (DBankAccount bankAccount : dBankAccounts) {
            bankAccounts.add(dBankAccountToBankAccount(bankAccount));
        }
        return bankAccounts;
    }

    public Set<BankAccountDTO> getAllForUser(User user) {
        List<DBankAccount> dBankAccounts = bankAccountRepo.findAllByUser(userService.userToDUser(user));

        Set<BankAccountDTO> bankAccounts = new HashSet<>();
        for (DBankAccount bankAccount : dBankAccounts) {
            bankAccounts.add(dBankAccountToBankAccount(bankAccount));
        }
        return bankAccounts;
    }

    public BankAccountDTO getByIdAndCurrentUser(String iban) {
        final DBankAccount bankAccount = bankAccountRepo.findByIdAndUser(iban, userService.getCurrentDUser());

        return dBankAccountToBankAccount(bankAccount);
    }

    public BankAccountDTO getByIdAndUser(String iban, User user) {
        final DBankAccount bankAccount = bankAccountRepo.findByIdAndUser(iban, userService.userToDUser(user));

        return dBankAccountToBankAccount(bankAccount);
    }

    public BankAccountDTO dBankAccountToBankAccount(DBankAccount account) {
        if (account == null)
            return null;

        return new BankAccountDTO(account.getIban(), account.getBic(), account.getUser().getId(),
                currencyService.dCurrencyToCurrency(account.getCurrency()));
    }

    public DBankAccount bankAccountToDBankAccount(BankAccountDTO account) {
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
