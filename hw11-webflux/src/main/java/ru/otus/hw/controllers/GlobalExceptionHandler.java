package ru.otus.hw.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.ErrorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public Mono<ResponseEntity<ErrorDto>> handleEntityNotFoundException(EntityNotFoundException ex) {
        var errorDto = new ErrorDto(HttpStatus.NOT_FOUND, ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ErrorDto>> handleValidationException(MethodArgumentNotValidException ex) {
        var errorDto = new ErrorDto(HttpStatus.BAD_REQUEST, getValidationErrorMessages(ex));
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorDto>> handleException(Exception ex) {

        log.error("Server error", ex);

        var errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto));
    }

    private String getValidationErrorMessages(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }
}
