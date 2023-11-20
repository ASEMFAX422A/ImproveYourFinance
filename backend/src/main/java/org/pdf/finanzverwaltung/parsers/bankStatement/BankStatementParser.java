package org.pdf.finanzverwaltung.parsers.bankStatement;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface BankStatementParser {

    public ParsedBankStatement parse(PDDocument document);

    public boolean canParse(PDDocument document);

}
