package org.pdf.finanzverwaltung.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.pdf.finanzverwaltung.dto.EditCategoryQuery;
import org.pdf.finanzverwaltung.dto.Transaction;
import org.pdf.finanzverwaltung.dto.TransactionCategory;
import org.pdf.finanzverwaltung.dto.User;
import org.pdf.finanzverwaltung.models.DTransaction;
import org.pdf.finanzverwaltung.models.DTransactionCategory;
import org.pdf.finanzverwaltung.repos.transaction.TransactionCategoryRepo;
import org.pdf.finanzverwaltung.repos.transaction.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TransactionCategoryRepo transactionCategoryRepo;

    public TransactionService() {
    }

    public boolean createForCurrentUser(EditCategoryQuery newCategory) {
        try {
            DTransactionCategory transactionCategory = new DTransactionCategory(userService.getCurrentDUser(),
                    newCategory.name, newCategory.matcherPattern, newCategory.matchDescription);
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

    public Set<Transaction> getAllBetweenForCurrentUser(Date startDate, Date endDate) {
        final Set<Transaction> transactions = new HashSet<>();

        for (DTransaction transaction : transactionRepo.findByUserAndDateBetween(userService.getCurrentDUser(),
                startDate, endDate)) {
            transactions.add(dTransactionToTransaction(transaction));
        }

        return transactions;
    }

    public TransactionCategory dCategoryToCategory(DTransactionCategory category) {
        return new TransactionCategory(category.getId(), category.getName(), category.getMatcherPattern());
    }

    public Transaction dTransactionToTransaction(DTransaction transaction) {
        if (transaction == null)
            return null;

        DTransactionCategory cat = transaction.getCategory();
        TransactionCategory category = null;

        if (cat != null)
            category = new TransactionCategory(cat.getId(), cat.getName(), cat.getMatcherPattern());

        return new Transaction(transaction.getId(), transaction.getDate(), transaction.getTitle(),
                transaction.getDescription(), transaction.getAmount(), category);
    }

    public DTransaction transactionToDTransaction(Transaction transaction) {
        if (transaction == null)
            return null;

        Optional<DTransaction> transactionOpt = transactionRepo.findById(transaction.getId());
        if (transactionOpt.isPresent())
            return transactionOpt.get();

        return null;
    }
}
