package org.pdf.finanzverwaltung.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.pdf.finanzverwaltung.dto.EditCategoryQuery;
import org.pdf.finanzverwaltung.dto.TransactionDTO;
import org.pdf.finanzverwaltung.dto.TransactionCategory;
import org.pdf.finanzverwaltung.dto.User;
import org.pdf.finanzverwaltung.models.DBankAccount;
import org.pdf.finanzverwaltung.models.DTransaction;
import org.pdf.finanzverwaltung.models.DTransactionCategory;
import org.pdf.finanzverwaltung.repos.bank.BankAccountRepo;
import org.pdf.finanzverwaltung.repos.transaction.TransactionCategoryRepo;
import org.pdf.finanzverwaltung.repos.transaction.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountRepo bankAccountRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TransactionCategoryRepo transactionCategoryRepo;

    public TransactionService() {
    }

    public boolean removeCategoryForCurrentUserByName(long id) {
        try {
            transactionCategoryRepo.deleteById(id);
            return true;
        } catch (Exception e) {
        }

        return false;
    }

    public boolean createOrEditCategoryForCurrentUser(EditCategoryQuery editCategory) {
        try {
            DTransactionCategory transactionCategory = transactionCategoryRepo
                    .findByUserAndName(userService.getCurrentDUser(), editCategory.name);
            if (transactionCategory != null) {
                transactionCategory.setName(editCategory.name);
                transactionCategory.setMatcherPattern(editCategory.matcherPattern);
                transactionCategory.setMatchDescription(editCategory.matchDescription);
            } else {
                transactionCategory = new DTransactionCategory(userService.getCurrentDUser(),
                        editCategory.name, editCategory.matcherPattern, editCategory.matchDescription);
            }

            transactionCategoryRepo.save(transactionCategory);
            return true;
        } catch (Exception e) {
        }

        return false;
    }

    public Set<TransactionCategory> getAllCategoriesForCurrentUser() {
        final List<DTransactionCategory> dCategories = transactionCategoryRepo
                .findAllByUser(userService.getCurrentDUser());

        final Set<TransactionCategory> categories = new HashSet<>();
        for (DTransactionCategory dCategory : dCategories) {
            categories.add(dCategoryToCategory(dCategory));
        }

        return categories;
    }

    public Set<TransactionCategory> getAllCategoriesForUser(User user) {
        List<DTransactionCategory> dCategories = transactionCategoryRepo.findAllByUser(userService.userToDUser(user));

        Set<TransactionCategory> categories = new HashSet<>();
        for (DTransactionCategory dCategory : dCategories) {
            categories.add(dCategoryToCategory(dCategory));
        }

        return categories;
    }

    public Set<TransactionDTO> getAllForBankAccountAndBetweenAndCurrentUser(String iban, Date startDate, Date endDate) {
        Optional<DBankAccount> bankAccountOpt = bankAccountRepo.findById(iban);
        if (bankAccountOpt.isEmpty())
            return null;

        final Set<TransactionDTO> transactions = new HashSet<>();

        for (DTransaction transaction : transactionRepo.findTransactionsByUserAndDateRangeAndBankAccount(
                userService.getCurrentDUser(),
                startDate, endDate, bankAccountOpt.get())) {
            transactions.add(dTransactionToTransaction(transaction));
        }

        return transactions;
    }

    public Set<TransactionDTO> getAllBetweenForCurrentUser(Date startDate, Date endDate) {
        final Set<TransactionDTO> transactions = new HashSet<>();

        for (DTransaction transaction : transactionRepo.findByUserAndDateBetween(userService.getCurrentDUser(),
                startDate, endDate)) {
            transactions.add(dTransactionToTransaction(transaction));
        }

        return transactions;
    }

    public TransactionCategory dCategoryToCategory(DTransactionCategory category) {
        return new TransactionCategory(category.getId(), category.getName(), category.getMatcherPattern());
    }

    public TransactionDTO dTransactionToTransaction(DTransaction transaction) {
        if (transaction == null)
            return null;

        DTransactionCategory cat = transaction.getCategory();
        TransactionCategory category = null;

        if (cat != null)
            category = new TransactionCategory(cat.getId(), cat.getName(), cat.getMatcherPattern());

        return new TransactionDTO(transaction.getId(), transaction.getDate(), transaction.getTitle(),
                transaction.getDescription(), transaction.getAmount(), category);
    }

    public DTransaction transactionToDTransaction(TransactionDTO transaction) {
        if (transaction == null)
            return null;

        Optional<DTransaction> transactionOpt = transactionRepo.findById(transaction.id);
        if (transactionOpt.isPresent())
            return transactionOpt.get();

        return null;
    }
}
