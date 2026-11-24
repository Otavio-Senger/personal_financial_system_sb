package com.otaviosenger.financial.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice //EXCEPTIONS
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) { //ResponseEntity: CUSTOMIZABLE HTTP RESPONSE
        return ResponseEntity.status(HttpStatus.NOT_FOUND) //404
                .body(Map.of("error", ex.getMessage())); // KEY, VALUE
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) //409
                .body(Map.of("error", "Operation not allowed: the data is in use by other records, or references something that does not exist."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //@Valid
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors() //getFieldErrors: RETRIEVES THE ERRORS FOUND IN THE FIELDS
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage())); //KEY, VALUE
                //forEach: ITERATES OVER THE ELEMENTS OF A COLLECTION
        //e: REPRESENTS EACH ERROR FOUND
        //e.getField(): RETRIEVES THE NOME OF THE FIELD THAT CAUSED THE ERROR
        //e.getDefaultMessage():RETRIEVES THE VALIDATION MESSAGE
        //errors.put(...): ADD THIS INFORMATION TO THE MAP
        return ResponseEntity.badRequest().body(errors); // 400
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid JSON. Check the syntax, that type REVENUE or EXPENSES, and that date is yyyy-MM-dd"));
    }

}
