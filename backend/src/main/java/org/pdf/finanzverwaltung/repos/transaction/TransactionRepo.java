package org.pdf.finanzverwaltung.repos.transaction;

import java.util.Date;
import java.util.List;

import org.pdf.finanzverwaltung.models.DBankAccount;
import org.pdf.finanzverwaltung.models.DTransaction;
import org.pdf.finanzverwaltung.models.DUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface TransactionRepo extends JpaRepository<DTransaction, Long> {

    public List<DTransaction> findByDateBetween(Date startDate, Date endDate);

    public List<DTransaction> findByUserAndDateBetween(DUser currentDUser, Date startDate, Date endDate);

    @Query("SELECT t FROM DTransaction t " +
            "WHERE t.user = :user " +
            "AND t.date BETWEEN :startDate AND :endDate " +
            "AND t.bankStatement.bankAccount = :bankAccount " +
            "ORDER BY t.date")
    public List<DTransaction> findTransactionsByUserAndDateRangeAndBankAccountSortByDate(
            @Param("user") DUser user,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("bankAccount") DBankAccount bankAccount);
}
