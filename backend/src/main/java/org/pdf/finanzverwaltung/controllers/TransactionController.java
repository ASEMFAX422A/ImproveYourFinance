package org.pdf.finanzverwaltung.controllers;

import java.util.Set;

import org.pdf.finanzverwaltung.dto.EditCategoryQuery;
import org.pdf.finanzverwaltung.dto.MessageDto;
import org.pdf.finanzverwaltung.dto.TransactionCategory;
import org.pdf.finanzverwaltung.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/categorys")
    public ResponseEntity<Set<TransactionCategory>> queryTransactionsCategorys() {
        final Set<TransactionCategory> categories = transactionService.getAllCategoriesForCurrentUser();

        return ResponseEntity.ok(categories);
    }

    @PostMapping("/create-category")
    public ResponseEntity<MessageDto> createCategory(@RequestBody EditCategoryQuery query) {
        final boolean created = transactionService.createForCurrentUser(query);
        if (created)
            return MessageDto.createResponse(HttpStatus.OK, "Category created");

        return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Category already exists");
    }
}
