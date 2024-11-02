package com.lincentpega.personalcrmjava.exception;

import com.lincentpega.personalcrmjava.controller.common.ValidationError;

import java.util.List;

public class PropertyValidationException extends ValidationException {

    public PropertyValidationException(String propertyName, String error) {
        super(List.of(new ValidationError(propertyName, error)));
    }
}
