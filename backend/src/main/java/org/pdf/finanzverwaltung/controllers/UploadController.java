package org.pdf.finanzverwaltung.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.pdf.finanzverwaltung.dto.MessageDto;
import org.pdf.finanzverwaltung.services.BankStatementService;
import org.pdf.finanzverwaltung.services.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/v1/upload")
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private BankStatementService bankStatementService;

    @Autowired
    private StorageService storageService;

    public UploadController() {
    }

    @PostMapping("/account-statement")
    public ResponseEntity<MessageDto> file(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "No file received");

        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Wrong file type");
        }

        try {
            File bankStatement = storageService.getNewRandomUserFilePath("bank-statements", ".pdf");
            if (bankStatement == null) {
                return MessageDto.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save file");
            }

            Files.copy(file.getInputStream(), bankStatement.toPath());

            if (bankStatementService.parseAndSave(bankStatement))
                return MessageDto.createResponse(HttpStatus.OK, "File successful uploaded");
            else
                bankStatement.delete();
        } catch (IOException e) {
            logger.error("Could not save file", e);
        }
        return MessageDto.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save file");
    }
}
