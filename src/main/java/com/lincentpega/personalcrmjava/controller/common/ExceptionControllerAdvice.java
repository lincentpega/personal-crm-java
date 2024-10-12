package com.lincentpega.personalcrmjava.controller.common;

import com.lincentpega.personalcrmjava.exception.ApplicationException;
import com.lincentpega.personalcrmjava.exception.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger log = LogManager.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        if (ex.hasFieldErrors()) {
            List<ValidationError> validationErrors = ex.getFieldErrors().stream()
                    .map(ExceptionControllerAdvice::toError)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest()
                    .body(new ErrorsResponse<>(validationErrors));
        }
        List<ErrorResponse> errors = ex.getAllErrors().stream()
                .map(ExceptionControllerAdvice::toError)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ErrorsResponse<>(errors));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e) {
        return ResponseEntity.badRequest().body(new ErrorsResponse<>(e.getErrors()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(ApplicationException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e);
        return ResponseEntity.internalServerError().build();
    }

    private static ValidationError toError(FieldError error) {
        var errorMessage = (error.getDefaultMessage() != null) ? error.getDefaultMessage() : "Invalid value";
        return new ValidationError(error.getField(), errorMessage);
    }

    private static ErrorResponse toError(ObjectError error) {
        var errorMessage = (error.getDefaultMessage() != null) ? error.getDefaultMessage() : (error.getObjectName() + " is invalid");
        return new ErrorResponse(errorMessage);
    }
}
