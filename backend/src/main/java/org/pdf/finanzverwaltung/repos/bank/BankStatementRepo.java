package org.pdf.finanzverwaltung.repos.bank;

import java.util.Date;
import java.util.List;

import org.pdf.finanzverwaltung.models.DBankAccount;
import org.pdf.finanzverwaltung.models.DBankStatement;
import org.pdf.finanzverwaltung.models.DUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BankStatementRepo extends JpaRepository<DBankStatement, Long> {
    
    public List<DBankStatement> findAllByUser(DUser user);
    
    public DBankStatement findByIdAndUser(long id, DUser user);

    public List<DBankStatement> findAllByBankAccount(DBankAccount dBankAccount);

    public DBankStatement findFirstByBankAccountOrderByIssuedDateDesc(DBankAccount bankAccount);

    public DBankStatement findFirstByIssuedDateAndBankAccount(Date date, DBankAccount bankAccount);

    public List<DBankStatement> findByUserAndIssuedDateBetween(DUser user, Date startDate, Date endDate);
    
    public DBankStatement findByUserAndBankAccountAndIssuedDateBetween(DUser currentDUser, DBankAccount account,
            Date startDate,
            Date endDate);
}
