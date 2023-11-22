package org.pdf.finanzverwaltung.controllers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pdf.finanzverwaltung.dto.BankAccountDTO;
import org.pdf.finanzverwaltung.dto.BankAccountOverviewDto;
import org.pdf.finanzverwaltung.dto.BankAccountOverviewQuery;
import org.pdf.finanzverwaltung.dto.BankAccountQuery;
import org.pdf.finanzverwaltung.dto.BankStatementDTO;
import org.pdf.finanzverwaltung.dto.TransactionDTO;
import org.pdf.finanzverwaltung.services.BankAccountService;
import org.pdf.finanzverwaltung.services.BankStatementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class BankAccountControllerTest {

    @Mock
    private BankStatementService bankStatementService;

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankAccountController bankAccountController;
    
    @Test
    public void testQueryBankAccounts() {
        Set<BankAccountDTO> mockBankAccounts = new HashSet<>();
        mockBankAccounts.add(new BankAccountDTO("iban", "bic", 1, null));
        when(bankAccountService.getAllForCurrentUser()).thenReturn(mockBankAccounts);

        ResponseEntity<Set<BankAccountDTO>> response = bankAccountController.queryBankAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBankAccounts, response.getBody());
    }
    
    @Test
    public void testSuccessfulQueryBankAccount() {
        BankAccountDTO mockBankAccount = new BankAccountDTO("iban", "bic", 1, null);
        when(bankAccountService.getByIdAndCurrentUser(mockBankAccount.getIban())).thenReturn(mockBankAccount);

        BankAccountQuery bankAccountQuery = new BankAccountQuery();
        bankAccountQuery.iban = mockBankAccount.getIban();

        ResponseEntity<BankAccountDTO> response = bankAccountController.queryBankAccount(bankAccountQuery);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBankAccount, response.getBody());
    }
    
    @Test
    public void testFailQueryBankAccount() {
        String mockIban = "iban";
        when(bankAccountService.getByIdAndCurrentUser(mockIban)).thenReturn(null);

        BankAccountQuery bankAccountQuery = new BankAccountQuery();
        bankAccountQuery.iban = mockIban;

        ResponseEntity<BankAccountDTO> response = bankAccountController.queryBankAccount(bankAccountQuery);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testQueryMonthForAllAccounts() {
        BankStatementDTO statement = new BankStatementDTO((long)1, new Date(), (double)5, (double)50, new HashSet<TransactionDTO>());
        Set<BankStatementDTO> statements = new HashSet<>(Arrays.asList(statement));

        when(bankStatementService.getByCurrentUserAndIssuedDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(statements);

        BankAccountOverviewQuery query = new BankAccountOverviewQuery();
        query.id = "all";
        query.start = new Date().getTime();
        query.end = new Date().getTime();

        ResponseEntity<BankAccountOverviewDto> response = bankAccountController.queryMonth(query);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(query.id, response.getBody().id);
        assertEquals(statement.getOldBalance(), response.getBody().startBalance, (double)0);
        assertEquals(statement.getNewBalance(), response.getBody().endBalance, (double)0);
        // TODO: Test erweitern
    }
}
