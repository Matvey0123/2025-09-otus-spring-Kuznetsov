package ru.otus.hw.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.dtos.ErrorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handeEntityNotFoundException(EntityNotFoundException ex) {

        log.error("Error find entity", ex);

        var modelAndView = new ModelAndView();
        modelAndView.setViewName("customError");
        modelAndView.addObject("error",
                new ErrorDto(HttpStatus.NOT_FOUND, ex.getMessage()));

        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handeValidationException(MethodArgumentNotValidException ex) {

        log.error("Validation error", ex);

        var modelAndView = new ModelAndView();
        modelAndView.setViewName("customError");
        modelAndView.addObject("error",
                new ErrorDto(HttpStatus.BAD_REQUEST, getValidationErrorMessages(ex)));

        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handeException(Exception ex) {

        log.error("Server error", ex);

        var modelAndView = new ModelAndView();
        modelAndView.setViewName("customError");
        modelAndView.addObject("error",
                new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));

        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }

    private String getValidationErrorMessages(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }
}
