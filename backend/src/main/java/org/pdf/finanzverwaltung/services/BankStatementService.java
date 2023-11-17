package org.pdf.finanzverwaltung.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.pdf.finanzverwaltung.dto.BankStatement;
import org.pdf.finanzverwaltung.parsers.BankStatementParser;
import org.pdf.finanzverwaltung.repos.bank.DBankStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankStatementService {
    private static final Logger logger = LoggerFactory.getLogger(BankStatementService.class);

    @Autowired
    private List<BankStatementParser> parser;

    public DBankStatement parse(PDDocument document) {
        for (BankStatementParser parser : parser) {
            if (parser.canParse(document))
                return parser.parse(document);
        }
        return null;
    }

    public boolean parseAndSave(File bankStatement) {
        // TODO Check Statement(date,iban) already exists

        try (PDDocument document = Loader.loadPDF(bankStatement)) {
            if (document.getNumberOfPages() <= 0) {
                return false;
            }

            return parse(document) != null;
        } catch (IOException e) {
            logger.error("Could not read pdf file", e);
        }
        return false;
    }

}
