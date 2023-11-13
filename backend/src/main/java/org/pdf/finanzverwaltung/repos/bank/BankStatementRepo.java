package org.pdf.finanzverwaltung.repos.bank;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BankStatementRepo
 */
@Repository
@Transactional(readOnly = true)
public interface BankStatementRepo extends JpaRepository<DBankStatement, Long> {

    public Optional<DBankStatement> findFirstByBankAccountOrderByIssuedDateDesc(DBankAccount bankAccount);
}
