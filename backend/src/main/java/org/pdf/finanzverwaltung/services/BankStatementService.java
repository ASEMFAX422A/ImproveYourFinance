package org.pdf.finanzverwaltung.services;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.pdf.finanzverwaltung.dto.BankStatementDTO;
import org.pdf.finanzverwaltung.dto.MessageDto;
import org.pdf.finanzverwaltung.dto.TransactionDTO;
import org.pdf.finanzverwaltung.dto.TransactionCategory;
import org.pdf.finanzverwaltung.dto.User;
import org.pdf.finanzverwaltung.models.DBankAccount;
import org.pdf.finanzverwaltung.models.DBankStatement;
import org.pdf.finanzverwaltung.models.DCurrency;
import org.pdf.finanzverwaltung.models.DTransaction;
import org.pdf.finanzverwaltung.models.DTransactionCategory;
import org.pdf.finanzverwaltung.models.DUser;
import org.pdf.finanzverwaltung.parsers.bankStatement.BankStatementParser;
import org.pdf.finanzverwaltung.parsers.bankStatement.ParsedBankStatement;
import org.pdf.finanzverwaltung.repos.bank.BankAccountRepo;
import org.pdf.finanzverwaltung.repos.bank.BankStatementRepo;
import org.pdf.finanzverwaltung.repos.currency.CurrencyRepo;
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
    private static final String BANK_STATEMENT_FOLDER = "/bank-statements";

    @Autowired
    private List<BankStatementParser> parsers;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrencyRepo currencyRepo;

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

            DBankAccount bankAccount = bankAccountRepo.findByIdAndUser(bankStatement.iban, dUser);
            if (bankAccount == null) {
                DCurrency cur = currencyRepo.findByShortName(bankStatement.currency.shortName);
                if (cur == null) {
                    cur = new DCurrency(bankStatement.currency.shortName, bankStatement.currency.longName);
                    currencyRepo.save(cur);
                }

                bankAccount = new DBankAccount(bankStatement.iban, bankStatement.bic, dUser, cur);
                bankAccountRepo.save(bankAccount);
            }

            final DBankStatement bankStatementOpt = bankStatementRepo
                    .findFirstByIssuedDateAndBankAccount(bankStatement.issuedDate, bankAccount);
            if (bankStatementOpt != null)
                return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Bank statement already exists");

            final DBankStatement newBankStatement = bankStatementRepo
                    .save(new DBankStatement(bankAccount, dUser, bankStatement.issuedDate,
                            bankStatement.oldBalance,
                            bankStatement.newBalance, bankStatementFile.getName()));

            final List<DTransactionCategory> categories = transactionCategoryRepo
                    .findAllByUser(dUser);

            for (DTransaction trans : bankStatement.transactions) {
                trans.setBankStatement(newBankStatement);
                trans.setUser(dUser);

                for (DTransactionCategory category : categories) {
                    if (category.getMatcherPattern() == null)
                        continue;

                    if (trans.getTitle().matches(category.getMatcherPattern()) || (category.isMatchDescription()
                            && Pattern.compile(category.getMatcherPattern(), Pattern.MULTILINE)
                                    .matcher(trans.getDescription()).find())) {
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

    public File getBankStatementFile(long id) {
        Optional<DBankStatement> bankStatementOpt = bankStatementRepo.findById(id);
        if (bankStatementOpt.isEmpty())
            return null;

        return new File(storageService.getUserDir() + BANK_STATEMENT_FOLDER, bankStatementOpt.get().getFileName());
    }

    public BankStatementDTO getById(long id) {
        final Optional<DBankStatement> bankStatementOpt = bankStatementRepo.findById(id);
        return dBankStatementToBankStatement(bankStatementOpt.get());
    }

    public BankStatementDTO getByUserAndBankAccountAndIssuedDateBetween(String id, Date startDate, Date endDate) {
        final DBankAccount bankAccount = bankAccountRepo.findByIdAndUser(id, userService.getCurrentDUser());
        if (bankAccount == null)
            return null;

        final DBankStatement bankStatement = bankStatementRepo
                .findByUserAndBankAccountAndIssuedDateBetween(userService.getCurrentDUser(), bankAccount, startDate,
                        endDate);
        return dBankStatementToBankStatement(bankStatement);
    }

    public Set<BankStatementDTO> getByCurrentUserAndIssuedDateBetween(Date startDate, Date endDate) {
        final List<DBankStatement> bankStatements = bankStatementRepo.findByUserAndIssuedDateBetween(
                userService.getCurrentDUser(), startDate, endDate);
        return dBankStatementsToBankStatements(bankStatements);
    }

    public BankStatementDTO getByIdAndCurrentUser(long id) {
        final DBankStatement bankStatement = bankStatementRepo.findByIdAndUser(id, userService.getCurrentDUser());
        return dBankStatementToBankStatement(bankStatement);
    }

    public BankStatementDTO getByIdAndUser(long id, User user) {
        final DBankStatement bankStatement = bankStatementRepo.findByIdAndUser(id, userService.userToDUser(user));
        return dBankStatementToBankStatement(bankStatement);
    }

    public Set<BankStatementDTO> getAllByIdAndCurrentUser(String iban) {
        return getAllByIdAndUser(iban, userService.getCurrentDUser());
    }

    public Set<BankStatementDTO> getAllByIdAndUser(String iban, User user) {
        return getAllByIdAndUser(iban, userService.userToDUser(user));
    }

    private Set<BankStatementDTO> getAllByIdAndUser(String iban, DUser user) {
        Set<BankStatementDTO> bankAccounts = new HashSet<>();

        DBankAccount bankAccountOpt = bankAccountRepo.findByIdAndUser(iban, user);
        if (bankAccountOpt == null)
            return bankAccounts;

        List<DBankStatement> dBankAccounts = bankStatementRepo.findAllByBankAccount(bankAccountOpt);
        for (DBankStatement dBankStatement : dBankAccounts) {
            bankAccounts.add(dBankStatementToBankStatement(dBankStatement));
        }

        return bankAccounts;
    }

    public Set<BankStatementDTO> dBankStatementsToBankStatements(List<DBankStatement> statements) {
        Set<BankStatementDTO> bankAccounts = new HashSet<>();

        if (statements == null)
            return bankAccounts;

        for (DBankStatement dBankStatement : statements) {
            bankAccounts.add(dBankStatementToBankStatement(dBankStatement));
        }

        return bankAccounts;
    }

    public BankStatementDTO dBankStatementToBankStatement(DBankStatement statement) {
        if (statement == null)
            return null;

        Set<TransactionDTO> transactions = new HashSet<>();
        for (DTransaction transaction : statement.getTransactions()) {

            DTransactionCategory cat = transaction.getCategory();
            TransactionCategory category = null;

            if (cat != null)
                category = new TransactionCategory(cat.getId(), cat.getName(), cat.getMatcherPattern());

            transactions.add(
                    new TransactionDTO(transaction.getId(), transaction.getDate(), transaction.getTitle(),
                            transaction.getDescription(), transaction.getAmount(), category));
        }

        return new BankStatementDTO(statement.getId(), statement.getIssuedDate(), statement.getOldBalance(),
                statement.getNewBalance(), transactions);
    }

    public DBankStatement bankStatementToDBankStatement(BankStatementDTO statement) {
        if (statement == null)
            return null;

        Optional<DBankStatement> bankStatementOpt = bankStatementRepo.findById(statement.getId());
        if (bankStatementOpt.isPresent())
            return bankStatementOpt.get();

        return null;
    }
}
