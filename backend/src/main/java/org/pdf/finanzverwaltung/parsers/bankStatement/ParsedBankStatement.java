package org.pdf.finanzverwaltung.parsers.bankStatement;

import java.util.Date;
import java.util.Set;

import org.pdf.finanzverwaltung.models.DTransaction;

public class ParsedBankStatement {
    public String iban;
    public Date issuedDate;
    public double oldBalance;
    public double newBalance;
    public Set<DTransaction> transactions;
}