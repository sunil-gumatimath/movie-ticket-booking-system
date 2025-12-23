package com.example.movieticketbookingsystem.exception.handler;

import com.example.movieticketbookingsystem.utility.ErrorStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@AllArgsConstructor
public class GeneralExceptionHandler {

    private final RestResponseBuilder responseBuilder;

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        String errorMessage = "Data integrity constraint violation";
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("Duplicate entry")) {
                errorMessage = "Duplicate entry detected. The resource already exists";
            } else if (ex.getMessage().contains("foreign key constraint")) {
                errorMessage = "Referenced resource does not exist";
            } else if (ex.getMessage().contains("not null")) {
                errorMessage = "Required field cannot be null";
            }
        }
        return responseBuilder.error(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleNoHandlerFoundException(NoHandlerFoundException ex){
        return responseBuilder.error(HttpStatus.NOT_FOUND, 
            String.format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
        return responseBuilder.error(HttpStatus.METHOD_NOT_ALLOWED, 
            String.format("HTTP method '%s' is not supported for this endpoint", ex.getMethod()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex){
        return responseBuilder.error(HttpStatus.BAD_REQUEST, 
            String.format("Required parameter '%s' of type '%s' is missing", ex.getParameterName(), ex.getParameterType()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        return responseBuilder.error(HttpStatus.BAD_REQUEST, 
            String.format("Invalid value '%s' for parameter '%s'. Expected type: %s", 
                ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleRuntimeException(RuntimeException ex){
        ex.printStackTrace();
        return responseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleException(Exception ex){
        ex.printStackTrace();
        return responseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
