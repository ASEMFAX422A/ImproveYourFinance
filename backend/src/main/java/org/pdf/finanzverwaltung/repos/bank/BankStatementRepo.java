package org.pdf.finanzverwaltung.repos.bank;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BankStatementRepo
 */
@Repository
@Transactional(readOnly = true)
public interface BankStatementRepo extends JpaRepository<DBankStatement, Long> {

}
