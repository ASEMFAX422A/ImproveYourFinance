package org.pdf.finanzverwaltung.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.pdf.finanzverwaltung.dto.Transaction;
import org.pdf.finanzverwaltung.repos.bank.BankAccountRepo;
import org.pdf.finanzverwaltung.repos.bank.BankStatementRepo;
import org.pdf.finanzverwaltung.repos.bank.DBankAccount;
import org.pdf.finanzverwaltung.repos.bank.DBankStatement;
import org.pdf.finanzverwaltung.repos.transaction.DTransaction;
import org.pdf.finanzverwaltung.repos.transaction.DTransactionCategory;
import org.pdf.finanzverwaltung.repos.transaction.TransactionCategoryRepo;
import org.pdf.finanzverwaltung.repos.transaction.TransactionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BankAccountRepo bankAccountRepo;

    @Autowired
    private TransactionCategoryRepo transactionCategoryRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private BankStatementRepo bankStatementRepo;

    public SparkasseParser() {
    }

    @Override
    public DBankStatement parse(PDDocument document) {
        final String pages = getPages(document, 1, document.getNumberOfPages() - 2);
        if (pages == null)
            return null;

        Date date = null;
        String iban = null;
        double oldBalance = -1;

        final StringBuilder transaction = new StringBuilder();
        final List<Transaction> transactions = new ArrayList<>();
        final Matcher matcher = datePattern.matcher(pages);

        int start = matcher.find() && matcher.find() ? matcher.start() : 0;

        final String startStr = pages.substring(0, start);

        date = getDate(startStr);
        iban = getIban(startStr);
        oldBalance = getOldBalance(startStr);

        // TODO check account exists -> create?
        // Use DTransaction???

        while (matcher.find()) {
            final Transaction trans = parseTransaction(transaction.append(pages.substring(start, matcher.start())));
            if (trans == null)
                break;
            transactions.add(trans);
            start = matcher.start();
        }

        final double newBalance = oldBalance + transactions.stream().mapToDouble(o -> o.getAmount()).sum();

        // TEST
        DTransactionCategory cat = transactionCategoryRepo.findById(1L).get();
        DBankAccount bankAccount = bankAccountRepo.findById(iban.trim()).get();

        DBankStatement bankStatement = new DBankStatement(bankAccount, date, oldBalance, newBalance, "LEL");
        bankStatementRepo.save(bankStatement);

        Set<DTransaction> tr = new HashSet<>();
        for (Transaction trans : transactions) {
            DTransaction dTransaction = new DTransaction(trans.getDate(), trans.getAmount(), bankStatement, cat);
            tr.add(dTransaction);
        }
        transactionRepo.saveAll(tr);
        // TETS END

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

    private static Transaction parseTransaction(StringBuilder transaction) {
        // TODO Category
        final Matcher endReachedMatcher = endReachedPattern.matcher(transaction.toString());
        if (endReachedMatcher.find())
            return null;

        Transaction trans = new Transaction();

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
        if (amountMatcher.find()) {
            // return error ???
        }

        final String amount = amountMatcher.group().trim();
        trans.setAmount(Double.parseDouble(amount.replaceAll("\\,", ".")));

        trans.setDescription(transaction.substring(0, amountMatcher.start()).toString());
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
