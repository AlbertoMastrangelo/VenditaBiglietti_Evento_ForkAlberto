package it.dedagroup.venditabiglietti.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BadRequestDTOResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String exception;
    private String message;
    private String path;
}
