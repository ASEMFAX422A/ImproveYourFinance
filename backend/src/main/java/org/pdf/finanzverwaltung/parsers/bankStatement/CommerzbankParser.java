package org.pdf.finanzverwaltung.parsers.bankStatement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.pdf.finanzverwaltung.models.DTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommerzbankParser implements BankStatementParser {
    private static final Logger logger = LoggerFactory.getLogger(CommerzbankParser.class);
    private static String bics[] = { "COBADEFFXXX" };

    private static final Pattern bicPattern = Pattern.compile("BIC\\s+:\\s*(\\S+)");
    private static final Pattern datePattern = Pattern.compile("\\d{2,}\\.\\d{2,}\\.\\d{4,}");
    private static final Pattern ibanPattern = Pattern.compile("IBAN:\\s*(.*?)\\s*(US|$)");
    private static final Pattern transactionPattern = Pattern.compile("\\d{2}\\.\\d{2} \\d+,\\d+-*$");
    private static final Pattern transactionEndReached = Pattern
            .compile("(^Folgeseite\\s+\\d+\\s+)|(^Buchungsdatum:\\s*\\d{2,}\\.\\d{2,}\\.\\d{4,})");
    private static final Pattern endReached = Pattern.compile("^Rechnungsabschluss\\s+\\d{2}.\\d{2}");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

    @Override
    public ParsedBankStatement parse(final PDDocument document) {
        final String pages = getPages(document, 1, document.getNumberOfPages());
        if (pages == null)
            return null;

        final ParsedBankStatement bankStatement = new ParsedBankStatement();
        final StringBuilder transaction = new StringBuilder();
        final Set<DTransaction> transactions = new HashSet<>();
        bankStatement.oldBalance = -1;
        boolean parsingTransaction = false;

        try (Scanner scanner = new Scanner(pages)) {
            scanner.useDelimiter("\\n");

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                final Matcher endReachedMatcher = endReached.matcher(line);
                if (endReachedMatcher.find()) {
                    transactions.add(parseTransaction(transaction, yearFormat.format(bankStatement.issuedDate)));
                    break;
                }

                if (bankStatement.issuedDate == null && line.startsWith("Kontoauszug vom")) {
                    bankStatement.issuedDate = getDate(line);
                }
                if (bankStatement.iban == null && line.startsWith("IBAN")) {
                    bankStatement.iban = getIban(line);
                }
                if (bankStatement.oldBalance == -1 && line.startsWith("Alter Kontostand")) {
                    bankStatement.oldBalance = getOldBalance(line);
                }

                if (parsingTransaction) {

                    final Matcher transactionEndMatcher = transactionEndReached.matcher(line);
                    if (transactionEndMatcher.find()) {
                        transactions.add(parseTransaction(transaction, yearFormat.format(bankStatement.issuedDate)));
                        parsingTransaction = false;
                    } else {
                        final Matcher newTransactionMatcher = transactionPattern.matcher(line);
                        if (newTransactionMatcher.find()) {
                            transactions
                                    .add(parseTransaction(transaction, yearFormat.format(bankStatement.issuedDate)));
                        }
                        transaction.append(line).append("\n");
                    }
                } else {
                    final Matcher newTransactionMatcher = transactionPattern.matcher(line);
                    if (newTransactionMatcher.find()) {
                        parsingTransaction = true;
                        transaction.append(line).append("\n");
                    }
                }
            }
        }

        bankStatement.transactions = transactions;
        bankStatement.newBalance = bankStatement.oldBalance
                + transactions.stream().mapToDouble(o -> o.getAmount()).sum();

        return bankStatement;
    }

    @Override
    public boolean canParse(final PDDocument document) {
        final String firstPage = getPages(document, 1, 1);
        if (firstPage == null)
            return false;

        final String bic = getBic(firstPage);
        if (bic == null)
            return false;

        for (final String knownBic : bics) {
            if (bic.equals(knownBic))
                return true;
        }

        return false;
    }

    private String getPages(final PDDocument document, final int startPage, final int endPage) {
        final PDFTextStripper extractor = new PDFTextStripper();
        extractor.setAverageCharTolerance(2);
        extractor.setSpacingTolerance(2);
        extractor.setStartPage(startPage);
        extractor.setEndPage(endPage);

        try {
            return extractor.getText(document);
        } catch (final Exception e) {
            logger.error("Could not extract text from pdf", e);
        }

        return null;
    }

    private static DTransaction parseTransaction(final StringBuilder transaction, final String year) {
        final DTransaction trans = new DTransaction();
        transaction.delete(transaction.length(), transaction.length());

        final int endLineIndex = transaction.indexOf("\n");
        final StringBuilder firstLine = new StringBuilder(transaction.substring(0, endLineIndex));
        transaction.delete(0, endLineIndex);

        final String amount = removeLastWord(firstLine);
        trans.setAmount(Double.parseDouble(amount.replaceAll("\\,", ".").replaceAll("\\-", "")));
        if (amount.endsWith("-"))
            trans.setAmount(trans.getAmount() * -1);

        final String date = removeLastWord(firstLine) + "." + year;
        try {
            trans.setDate(dateFormat.parse(date));
        } catch (final ParseException e) {
            logger.error("Could not parse date: " + date, e);
        }

        trans.setTitle(firstLine.toString());
        trans.setDescription(transaction.toString().trim());

        transaction.delete(0, transaction.length());

        return trans;
    }

    private static String removeLastWord(final StringBuilder stringBuilder) {
        final int lastSpace = stringBuilder.lastIndexOf(" ");
        final String lastWord = stringBuilder.substring(lastSpace + 1);

        stringBuilder.delete(lastSpace, stringBuilder.length());

        return lastWord;
    }

    private static double getOldBalance(final String line) {
        final int lastSpace = line.lastIndexOf(' ');
        return Double.parseDouble(line.substring(lastSpace + 1).replaceAll("\\,", "."));
    }

    private static String getBic(final String line) {
        final Matcher matcher = bicPattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private static String getIban(final String line) {
        final Matcher matcher = ibanPattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private static Date getDate(final String line) {
        final Matcher matcher = datePattern.matcher(line);

        if (matcher.find()) {
            try {
                return dateFormat.parse(matcher.group());
            } catch (final ParseException e) {
                logger.error("Could not parse date: " + matcher.group(), e);
            }
        }

        return null;
    }
}