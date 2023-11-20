package org.pdf.finanzverwaltung.repos.bank;

import java.util.List;
import java.util.Optional;

import org.pdf.finanzverwaltung.models.DBankAccount;
import org.pdf.finanzverwaltung.models.DUser;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BankAccountRepo extends JpaRepository<DBankAccount, String> {

    public boolean existsByIdAndUserId(String iban, DUser user);

    public List<DBankAccount> findAllByUser(DUser user);

    public Optional<DBankAccount> findByIdAndUser(String iban, DUser user);
}
