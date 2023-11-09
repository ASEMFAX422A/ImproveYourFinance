package org.pdf.finanzverwaltung.repos.transaction;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * TransactionCategoryRepo
 */
@Repository
@Transactional(readOnly = true)
public interface TransactionCategoryRepo extends JpaRepository<DTransactionCategory, Long> {

    public Optional<DTransactionCategory> findByName(String name);

}
