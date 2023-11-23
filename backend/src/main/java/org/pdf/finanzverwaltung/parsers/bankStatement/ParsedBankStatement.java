package org.pdf.finanzverwaltung.parsers.bankStatement;

import java.util.Date;
import java.util.Set;

import org.pdf.finanzverwaltung.dto.CurrencyDTO;
import org.pdf.finanzverwaltung.models.DTransaction;

public class ParsedBankStatement {
    public String iban;
    public String bic;
    public Date issuedDate;
    public double oldBalance;
    public double newBalance;
    public CurrencyDTO currency;
    public Set<DTransaction> transactions;
}
