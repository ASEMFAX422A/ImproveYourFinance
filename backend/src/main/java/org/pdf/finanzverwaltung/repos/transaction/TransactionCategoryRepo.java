package org.pdf.finanzverwaltung.repos.transaction;

import java.util.List;

import org.pdf.finanzverwaltung.models.DTransactionCategory;
import org.pdf.finanzverwaltung.models.DUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface TransactionCategoryRepo extends JpaRepository<DTransactionCategory, Long> {

    public DTransactionCategory findByName(String name);

    public List<DTransactionCategory> findAllByUser(DUser user);

    public DTransactionCategory findByUserAndName(DUser user, String name);
}
