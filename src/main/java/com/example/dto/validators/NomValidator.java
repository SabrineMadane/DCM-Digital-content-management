package com.example.dto.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NomValidator implements ConstraintValidator<ValidNom, String> {
    @Override
    public void initialize(ValidNom constraintAnnotation) {
    }

    @Override
    public boolean isValid(String nom, ConstraintValidatorContext context) {
        return nom != null && nom.length() >= 3;
    }
    }
