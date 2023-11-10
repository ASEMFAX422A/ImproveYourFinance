package org.pdf.finanzverwaltung.repos.currency;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * CurrencyRepo
 */

@Repository
@Transactional(readOnly = true)
public interface CurrencyRepo extends JpaRepository<DCurrency, Long> {

    public Optional<DCurrency> findByShortName(String shortName);

}
