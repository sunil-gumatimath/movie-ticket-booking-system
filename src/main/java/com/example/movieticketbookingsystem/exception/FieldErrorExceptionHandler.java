package com.example.movieticketbookingsystem.exception;

import com.example.movieticketbookingsystem.utility.FieldErrorStructure;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@ControllerAdvice
public class FieldErrorExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<ObjectError> errors = ex.getBindingResult().getAllErrors();

        List<CustomFieldError> customFieldErrors = new LinkedList<>();

        for (ObjectError error : errors) {
            if (error instanceof FieldError fieldError){
                customFieldErrors.add(CustomFieldError.builder()
                        .field(fieldError.getField())
                        .rejectedValue(String.valueOf(fieldError.getRejectedValue()))
                        .errorMessage(fieldError.getDefaultMessage())
                        .build());
            }
        }

        FieldErrorStructure<List<CustomFieldError>> errorResponse =
                FieldErrorStructure.<List<CustomFieldError>>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .errorMessage("Invalid Input")
                        .data(customFieldErrors)
                        .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }

    @Builder
    @Getter
    @ToString
    public static class CustomFieldError {
        String field;
        Object rejectedValue;
        String errorMessage;
    }
}