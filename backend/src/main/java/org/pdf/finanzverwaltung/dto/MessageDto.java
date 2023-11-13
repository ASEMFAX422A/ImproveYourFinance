package org.pdf.finanzverwaltung.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * MessageDto
 */
public class MessageDto {
    public String message;

    public MessageDto(String message) {
        this.message = message;
    }

    public static ResponseEntity<MessageDto> createResponse(HttpStatus status, String msg) {
        return ResponseEntity.status(status).body(new MessageDto(msg));
    }
}
