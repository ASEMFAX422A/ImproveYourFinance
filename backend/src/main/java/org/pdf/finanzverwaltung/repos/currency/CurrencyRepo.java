package org.pdf.finanzverwaltung.repos.currency;

import org.pdf.finanzverwaltung.models.DCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CurrencyRepo extends JpaRepository<DCurrency, Long> {

    public DCurrency findByShortName(String shortName);

}
