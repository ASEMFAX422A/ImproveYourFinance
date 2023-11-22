package org.pdf.finanzverwaltung.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.pdf.finanzverwaltung.dto.BankAccountQuery;
import org.pdf.finanzverwaltung.dto.BankStatementDTO;
import org.pdf.finanzverwaltung.dto.BankStatementQuery;
import org.pdf.finanzverwaltung.dto.MessageDto;
import org.pdf.finanzverwaltung.services.BankStatementService;
import org.pdf.finanzverwaltung.services.StorageService;
import org.pdf.finanzverwaltung.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/bank-statement")
public class BankStatementController {
    private static final Logger logger = LoggerFactory.getLogger(BankStatementController.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private BankStatementService bankStatementService;

    @GetMapping("/get-pdf")
    public ResponseEntity<ByteArrayResource> getPdf(@RequestParam long id) {
        final BankStatementDTO bankStatement = bankStatementService.getByIdAndCurrentUser(id);

        if (bankStatement == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

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
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=Kontoauszug_" + dateFormat.format(bankStatement.getIssuedDate()) + ".pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(fileBytes.length);

        return ResponseEntity.ok().headers(headers).body(fileBytesResource);
    }

    @PostMapping("/query-statement")
    public ResponseEntity<BankStatementDTO> queryBankStatement(@RequestBody BankStatementQuery query) {
        final BankStatementDTO bankStatement = bankStatementService.getByIdAndCurrentUser(query.id);

        if (bankStatement == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        return ResponseEntity.ok(bankStatement);
    }

    @PostMapping("/query-statements")
    public ResponseEntity<Set<BankStatementDTO>> queryBankStatements(@RequestBody BankAccountQuery query) {
        final Set<BankStatementDTO> bankStatements = bankStatementService.getAllByIdAndCurrentUser(query.iban);

        return ResponseEntity.ok(bankStatements);
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageDto> file(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "No file received");

        if (file.getOriginalFilename() == null || !file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Wrong file type");
        }

        try {
            File bankStatement = storageService.getNewRandomUserFilePath("bank-statements", ".pdf");
            if (bankStatement == null) {
                return MessageDto.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save file");
            }

            Files.copy(file.getInputStream(), bankStatement.toPath());

            final ResponseEntity<MessageDto> response = bankStatementService.parseAndSave(userService.getCurrentUser(),
                    bankStatement);
            if (response.getStatusCode() != HttpStatus.OK)
                bankStatement.delete();

            return response;
        } catch (IOException e) {
            logger.error("Could not save file", e);
        }
        return MessageDto.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save file");
    }
}
