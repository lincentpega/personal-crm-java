package com.lincentpega.personalcrmjava.controller.validator;

import com.lincentpega.personalcrmjava.domain.person.PersonGender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<ValidGender, String> {

    @Override
    public boolean isValid(String gender, ConstraintValidatorContext context) {
        if (gender == null)
            return true;

        try {
            PersonGender.valueOf(gender.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
