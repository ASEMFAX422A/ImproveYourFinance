package org.pdf.finanzverwaltung.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.pdf.finanzverwaltung.dto.User;
import org.pdf.finanzverwaltung.services.BankStatementService;
import org.pdf.finanzverwaltung.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bank-statement")
public class BankStatementController {
    private static final Logger logger = LoggerFactory.getLogger(BankStatementController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BankStatementService bankStatementService;

    @GetMapping("/view")
    public ResponseEntity<ByteArrayResource> getPdf(@RequestParam long id) {
        // TODO File name use date, Check user
        final User user = userService.getCurrentUser();
        final File bankStatementFile = bankStatementService.getBankStatementFile(id);

        byte[] fileBytes = null;
        try {
            fileBytes = Files.readAllBytes(bankStatementFile.toPath());
        } catch (IOException e) {
            logger.error("Could not read pdf file", e);
            return ResponseEntity.badRequest().body(null);
        }
        ByteArrayResource fileBytesResource = new ByteArrayResource(fileBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=Kontoauszug.pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(fileBytes.length);

        return ResponseEntity.ok().headers(headers).body(fileBytesResource);
    }
}
