package org.pdf.finanzverwaltung.parsers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.pdf.finanzverwaltung.repos.bank.DBankStatement;

/**
 * BankStatementParser
 */
public interface BankStatementParser {

    public DBankStatement parse(PDDocument document);

    public boolean canParse(PDDocument document);

}
