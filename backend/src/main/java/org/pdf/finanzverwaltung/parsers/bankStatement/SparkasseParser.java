package org.pdf.finanzverwaltung.parsers.bankStatement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.pdf.finanzverwaltung.dto.CurrencyDTO;
import org.pdf.finanzverwaltung.models.DTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SparkasseParser implements BankStatementParser {
    private static final Logger logger = LoggerFactory.getLogger(SparkasseParser.class);
    private static String bics[] = { "WELADED1KSD" };

    private static final Pattern bicPattern = Pattern.compile("BIC\\s*:\\s*(\\S+)");
    private static final Pattern ibanPattern = Pattern.compile(", (DE.*)");
    private static final Pattern amountPattern = Pattern.compile("\\s+-*\\d+,\\d+");
    private static final Pattern dateNamePattern = Pattern.compile("\\d+\\.\\D+ \\d{4,}");
    private static final Pattern datePattern = Pattern.compile("\\d{2,}\\.\\d{2,}\\.\\d{4,}");
    private static final Pattern endReachedPattern = Pattern
            .compile("\\d{2,}\\.\\d{2,}\\.\\d{4,} Entgeltabrechnung / Wert:\\s*$");

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
    private static final SimpleDateFormat dateStatementFormat = new SimpleDateFormat("dd. MMMM yyyy", Locale.GERMANY);

    public SparkasseParser() {
    }

    @Override
    public ParsedBankStatement parse(PDDocument document) {
        final String pages = getPages(document, 1, document.getNumberOfPages() - 2);
        if (pages == null)
            return null;

        final ParsedBankStatement bankStatement = new ParsedBankStatement();
        final Matcher dateMatcher = datePattern.matcher(pages);

        int start = dateMatcher.find() && dateMatcher.find() ? dateMatcher.start() : 0;
        final String startStr = pages.substring(0, start);

        bankStatement.bic = getBic(pages);
        bankStatement.iban = getIban(startStr);
        bankStatement.issuedDate = getDate(startStr);
        bankStatement.oldBalance = getOldBalance(startStr);
        bankStatement.currency = new CurrencyDTO("€", "Euro");
        bankStatement.transactions = getTransactions(pages, dateMatcher, start);
        bankStatement.newBalance = bankStatement.oldBalance
                + bankStatement.transactions.stream().mapToDouble(o -> o.getAmount()).sum();
        bankStatement.newBalance = Math.round(bankStatement.newBalance * 100.0) / 100.0;

        return bankStatement;
    }

    @Override
    public boolean canParse(PDDocument document) {
        final String firstPage = getPages(document, 1, 1);
        if (firstPage == null)
            return false;

        String bic = getBic(firstPage);
        if (bic == null)
            return false;

        for (String knownBic : bics) {
            if (bic.equals(knownBic))
                return true;
        }

        return false;
    }

    private Set<DTransaction> getTransactions(String pages, Matcher dateMatcher, int start) {
        final StringBuilder transaction = new StringBuilder();
        final Set<DTransaction> transactions = new HashSet<>();

        while (dateMatcher.find()) {
            final DTransaction trans = parseTransaction(
                    transaction.append(pages.substring(start, dateMatcher.start())));
            if (trans == null)
                break;

            transactions.add(trans);
            start = dateMatcher.start();
        }
        return transactions;
    }

    private static DTransaction parseTransaction(StringBuilder transaction) {
        final Matcher endReachedMatcher = endReachedPattern.matcher(transaction.toString());
        if (endReachedMatcher.find())
            return null;

        DTransaction trans = new DTransaction();

        final StringBuilder firstLine = new StringBuilder(getFirstLine(transaction));
        final int spaceIndex = firstLine.indexOf(" ");

        final String date = firstLine.substring(0, spaceIndex);
        try {
            trans.setDate(dateFormat.parse(date));
        } catch (ParseException e) {
            logger.error("Could not parse date: " + date, e);
        }
        trans.setTitle(firstLine.substring(spaceIndex + 1));

        final Matcher amountMatcher = amountPattern.matcher(transaction.toString());
        if (!amountMatcher.find()) {
            // TODO return error ???
        }

        final String amount = amountMatcher.group().trim();
        trans.setAmount(Double.parseDouble(amount.replaceAll("\\,", ".")));

        trans.setDescription(transaction.substring(0, amountMatcher.start()).toString().trim());
        transaction.delete(0, transaction.length());

        return trans;
    }

    private String getPages(PDDocument document, int startPage, int endPage) {
        PDFTextStripper extractor = new PDFTextStripper();
        extractor.setStartPage(startPage);
        extractor.setEndPage(endPage);

        try {
            return extractor.getText(document);
        } catch (Exception e) {
            logger.error("Could not extract text from pdf", e);
        }

        return null;
    }

    private static String getFirstLine(StringBuilder transaction) {
        final int endLineIndex = transaction.indexOf("\n");
        final String firstLine = transaction.substring(0, endLineIndex);
        transaction.delete(0, endLineIndex);
        return firstLine;
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
            return matcher.group(1).trim();
        }
        return null;
    }

    private Date getDate(String page) {
        Matcher matcher = dateNamePattern.matcher(page);

        if (matcher.find()) {
            try {
                return dateStatementFormat.parse(matcher.group());
            } catch (ParseException e) {
                logger.error("Could not parse date: " + matcher.group(), e);
            }
        }
        return null;
    }

    private static double getOldBalance(String line) {
        final int lastSpace = line.lastIndexOf(' ');
        return Double.parseDouble(line.substring(lastSpace + 1).replaceAll("\\,", "."));
    }
}
