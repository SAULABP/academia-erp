package com.academia.academiaerp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDTO {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String mensaje;

    public ErrorResponseDTO(int status, String error, String mensaje) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
    }
}