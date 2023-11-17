package org.pdf.finanzverwaltung.services;

import java.util.Set;

import org.pdf.finanzverwaltung.dto.TransactionCategory;
import org.pdf.finanzverwaltung.models.User;
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

    public Set<TransactionCategory> getAllCategorysForUser(User user) {
        return null;
    }
}
