package org.pdf.finanzverwaltung.repos.bank;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.pdf.finanzverwaltung.models.DBankAccount;
import org.pdf.finanzverwaltung.models.DBankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BankStatementRepo extends JpaRepository<DBankStatement, Long> {

    public List<DBankStatement> findAllByBankAccount(DBankAccount dBankAccount);

    public Optional<DBankStatement> findFirstByBankAccountOrderByIssuedDateDesc(DBankAccount bankAccount);

    public Optional<DBankStatement> findFirstByIssuedDateAndBankAccount(Date date, DBankAccount bankAccount);
}
