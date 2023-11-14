package org.pdf.finanzverwaltung.parsers;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pdf.finanzverwaltung.repos.bank.DBankStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;

/**
 * SparkasseParser
 */
@Component
public class SparkasseParser implements BankStatementParser {
    private static final Logger logger = LoggerFactory.getLogger(SparkasseParser.class);
    private static String bics[] = { "WELADED1KSD" };

    private static final Pattern bicPattern = Pattern.compile("BIC\\s*:\\s*(\\S+)");
    private static final Pattern ibanPattern = Pattern.compile(",\\s*(([A-Za-z0-9]+\\s)+)");
    private static final Pattern datePattern = Pattern.compile("(\\d+\\.\\s*\\w+\\s*\\d{4})");

    @Override
    public DBankStatement parse(PdfReader document) {
        PdfTextExtractor extractor = new PdfTextExtractor(document);
        String date = null;
        String iban = null;
        String bic = null;

        try {
            String page = extractor.getTextFromPage(1);
            date = getDate(page);
            iban = getIban(page);
            bic = getBic(page);

        } catch (Exception e) {
            logger.error("Could not extract text from pdf", e);
        }

        logger.info(bic);
        logger.info(iban);
        logger.info(date);
        return null;
    }

    @Override
    public boolean canParse(PdfReader document) {
        PdfTextExtractor extractor = new PdfTextExtractor(document);
        String firstPage;
        try {
            firstPage = extractor.getTextFromPage(1);
        } catch (IOException e) {
            logger.error("Could not extract page", e);
            return false;
        }

        String bic = getBic(firstPage);
        if (bic == null)
            return false;

        for (String knownBic : bics) {
            if (bic.equals(knownBic))
                return true;

        }
        return false;
    }

    private String getBic(String page) {
        Matcher matcher = bicPattern.matcher(page);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getIban(String page) {
        Matcher matcher = ibanPattern.matcher(page);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getDate(String page) {
        Matcher matcher = datePattern.matcher(page);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
