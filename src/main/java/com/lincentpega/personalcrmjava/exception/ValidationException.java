package com.lincentpega.personalcrmjava.exception;

import com.lincentpega.personalcrmjava.controller.common.ValidationError;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private final List<ValidationError> errors;

    public ValidationException(List<ValidationError> errors) {
        this.errors = errors;
    }
}
