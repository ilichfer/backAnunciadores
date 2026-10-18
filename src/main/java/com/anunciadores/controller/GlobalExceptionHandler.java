package com.anunciadores.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Manejo centralizado de errores: el detalle tecnico va al log y al cliente
 * solo le llega un mensaje entendible (nunca la excepcion interna).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleStatus(ResponseStatusException e) {
        String reason = e.getReason() != null ? e.getReason() : e.getStatus().getReasonPhrase();
        return ResponseEntity.status(e.getStatus()).body(reason);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        String mensaje = error != null ? error.getDefaultMessage() : "Datos inválidos";
        return ResponseEntity.badRequest().body(mensaje);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<String> handleBadRequest(Exception e) {
        LOGGER.warn("Petición inválida: {}", e.getMessage());
        return ResponseEntity.badRequest().body("Datos inválidos en la solicitud");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSize(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("La imagen supera el tamaño máximo permitido (20 MB)");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpected(Exception e) {
        LOGGER.error("Error no controlado", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocurrió un error inesperado. Intenta de nuevo en unos minutos.");
    }
}
