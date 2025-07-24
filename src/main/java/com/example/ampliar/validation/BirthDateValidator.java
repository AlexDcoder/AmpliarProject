package com.example.ampliar.validation;

import java.time.LocalDate;

import com.example.ampliar.validation.constraints.BirthDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BirthDateValidator implements ConstraintValidator<BirthDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return false;
        }

        return birthDate.isBefore(LocalDate.now());
    }
}

