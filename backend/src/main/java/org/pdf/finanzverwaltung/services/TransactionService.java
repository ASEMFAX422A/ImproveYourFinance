package org.pdf.finanzverwaltung.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private TransactionRepo transactionRepo;

    @Autowired
    private TransactionCategoryRepo transactionCategoryRepo;

    @Autowired
    private UserService userService;

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

        // TODO bank account check
        for (DTransaction transaction : transactionRepo.findByDateBetween(startDate, endDate)) {
            transactions.add(dTransactionToTransaction(transaction));
        }

        return transactions;
    }

    public Set<Transaction> getByIdBetweenForCurrentUser(String id, Date startDate, Date endDate) {
        final Set<Transaction> transactions = new HashSet<>();

        // TODO Get
        // for (DTransaction transaction : transactionRepo.findByDateBetween(startDate,
        // endDate)) {
        // transactions.add(dTransactionToTransaction(transaction));
        // }

        return transactions;
    }

    public TransactionCategory dCategoryToCategory(DTransactionCategory category) {
        return new TransactionCategory(category.getId(), category.getName(), category.getMatcherPattern());
    }

    public Transaction dTransactionToTransaction(DTransaction transaction) {
        DTransactionCategory cat = transaction.getCategory();
        TransactionCategory category = null;

        if (cat != null)
            category = new TransactionCategory(cat.getId(), cat.getName(), cat.getMatcherPattern());

        return new Transaction(transaction.getId(), transaction.getDate(), transaction.getTitle(),
                transaction.getDescription(), transaction.getAmount(), category);
    }

    public DTransaction transactionToDTransaction(Transaction trans) {
        Optional<DTransaction> transactionOpt = transactionRepo.findById(trans.getId());
        if (transactionOpt.isPresent())
            return transactionOpt.get();

        return null;
    }
}
