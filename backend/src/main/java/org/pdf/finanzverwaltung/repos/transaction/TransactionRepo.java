package org.pdf.finanzverwaltung.repos.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * TransactionRepo
 */
@Repository
@Transactional(readOnly = true)
public interface TransactionRepo extends JpaRepository<DTransaction, Long> {

}
