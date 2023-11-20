package org.pdf.finanzverwaltung.services;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.pdf.finanzverwaltung.dto.BankStatement;
import org.pdf.finanzverwaltung.dto.MessageDto;
import org.pdf.finanzverwaltung.dto.Transaction;
import org.pdf.finanzverwaltung.dto.TransactionCategory;
import org.pdf.finanzverwaltung.dto.User;
import org.pdf.finanzverwaltung.models.DBankAccount;
import org.pdf.finanzverwaltung.models.DBankStatement;
import org.pdf.finanzverwaltung.models.DTransaction;
import org.pdf.finanzverwaltung.models.DTransactionCategory;
import org.pdf.finanzverwaltung.models.DUser;
import org.pdf.finanzverwaltung.parsers.bankStatement.BankStatementParser;
import org.pdf.finanzverwaltung.parsers.bankStatement.ParsedBankStatement;
import org.pdf.finanzverwaltung.repos.bank.BankAccountRepo;
import org.pdf.finanzverwaltung.repos.bank.BankStatementRepo;
import org.pdf.finanzverwaltung.repos.transaction.TransactionCategoryRepo;
import org.pdf.finanzverwaltung.repos.transaction.TransactionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BankStatementService {
    private static final Logger logger = LoggerFactory.getLogger(BankStatementService.class);

    @Autowired
    private List<BankStatementParser> parsers;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private BankAccountRepo bankAccountRepo;

    @Autowired
    private BankStatementRepo bankStatementRepo;

    @Autowired
    private TransactionCategoryRepo transactionCategoryRepo;

    public BankStatementService() {
    }

    public ParsedBankStatement parse(PDDocument document) {
        for (BankStatementParser parser : parsers) {
            if (parser.canParse(document))
                return parser.parse(document);
        }
        return null;
    }

    public ResponseEntity<MessageDto> parseAndSave(User user, File bankStatementFile) {
        try (PDDocument document = Loader.loadPDF(bankStatementFile)) {
            if (document.getNumberOfPages() <= 0) {
                return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Invalid PDF file");
            }

            final ParsedBankStatement bankStatement = parse(document);
            if (bankStatement == null)
                return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Invalid PDF file");

            final DUser dUser = userService.userToDUser(user);

            final Optional<DBankAccount> bankAccount = bankAccountRepo.findByIdAndUser(bankStatement.iban, dUser);
            if (!bankAccount.isPresent())
                return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "No bank account found for bank statement");

            final Optional<DBankStatement> bankStatementOpt = bankStatementRepo
                    .findFirstByIssuedDateAndBankAccount(bankStatement.issuedDate, bankAccount.get());
            if (bankStatementOpt.isPresent())
                return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Bank statement already exists");

            final DBankStatement newBankStatement = bankStatementRepo
                    .save(new DBankStatement(bankAccount.get(), bankStatement.issuedDate, bankStatement.oldBalance,
                            bankStatement.newBalance, bankStatementFile.getName()));

            final List<DTransactionCategory> categories = transactionCategoryRepo
                    .findAllByUser(dUser);

            for (DTransaction trans : bankStatement.transactions) {
                trans.setBankStatement(newBankStatement);

                for (DTransactionCategory category : categories) {
                    if (category.getMatcherPattern() == null)
                        continue;

                    if (trans.getTitle().matches(category.getMatcherPattern())) {
                        trans.setCategory(category);
                        break;
                    }
                }
            }
            transactionRepo.saveAll(bankStatement.transactions);

            return MessageDto.createResponse(HttpStatus.OK, "Bank statement added");
        } catch (IOException e) {
            logger.error("Could not read pdf file", e);
        }

        return MessageDto.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown parse error");
    }

    public BankStatement getById(long id) {
        Optional<DBankStatement> bankStatementOpt = bankStatementRepo.findById(id);
        if (bankStatementOpt.isEmpty())
            return null;

        return dBankStatementToBankStatement(bankStatementOpt.get());
    }

    public File getBankStatementFile(long id) {
        Optional<DBankStatement> bankStatementOpt = bankStatementRepo.findById(id);
        if (bankStatementOpt.isEmpty())
            return null;

        return new File(storageService.getUserDir() + "/bank-statements", bankStatementOpt.get().getFileName());
    }

    public Set<BankStatement> getAllByIbanAndUser(String iban, User user) {
        Set<BankStatement> bankAccounts = new HashSet<>();

        Optional<DBankAccount> bankAccountOpt = bankAccountRepo.findById(iban);
        if (!bankAccountOpt.isPresent())
            return bankAccounts;

        if (bankAccountOpt.get().getUser().getId() != user.getId())
            return bankAccounts;

        List<DBankStatement> dBankAccounts = bankStatementRepo.findAllByBankAccount(bankAccountOpt.get());
        for (DBankStatement dBankStatement : dBankAccounts) {
            bankAccounts.add(dBankStatementToBankStatement(dBankStatement));
        }

        return bankAccounts;
    }

    public BankStatement dBankStatementToBankStatement(DBankStatement statement) {
        Set<Transaction> transactions = new HashSet<>();
        for (DTransaction transaction : statement.getTransactions()) {

            DTransactionCategory cat = transaction.getCategory();
            TransactionCategory category = null;

            if (cat != null)
                category = new TransactionCategory(cat.getId(), cat.getName(), cat.getMatcherPattern());

            transactions.add(
                    new Transaction(transaction.getId(), transaction.getDate(), transaction.getTitle(),
                            transaction.getDescription(), transaction.getAmount(), category));
        }

        return new BankStatement(statement.getId(), statement.getIssuedDate(), statement.getOldBalance(),
                statement.getNewBalance(), transactions);
    }

    public DBankStatement bankStatementToDBankStatement(BankStatement statement) {
        Optional<DBankStatement> bankStatementOpt = bankStatementRepo.findById(statement.getId());
        if (bankStatementOpt.isPresent())
            return bankStatementOpt.get();

        return null;
    }
}
