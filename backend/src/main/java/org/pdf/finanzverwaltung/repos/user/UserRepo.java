package org.pdf.finanzverwaltung.repos.user;

import java.util.Optional;

import org.pdf.finanzverwaltung.models.DUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface UserRepo extends JpaRepository<DUser, Long> {

    public Optional<DUser> findByUsername(String username);
}
