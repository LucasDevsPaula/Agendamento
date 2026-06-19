package com.lucas.agendamento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.NOT_FOUND.value())
            .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.BAD_REQUEST.value())
            .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.FORBIDDEN.value())
            .build();
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
  }
}
