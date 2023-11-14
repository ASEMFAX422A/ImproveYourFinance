package org.pdf.finanzverwaltung.parsers;

import org.pdf.finanzverwaltung.repos.bank.DBankStatement;

import com.lowagie.text.pdf.PdfReader;

/**
 * BankStatementParser
 */
public interface BankStatementParser {

    public DBankStatement parse(PdfReader document);

    public boolean canParse(PdfReader document);

}
