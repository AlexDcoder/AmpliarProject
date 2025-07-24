package com.example.ampliar.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

import com.example.ampliar.validation.constraints.AppointmentDate;

public class AppointmentDateValidator implements ConstraintValidator<AppointmentDate, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime appointmentDate, ConstraintValidatorContext context) {
        if (appointmentDate == null) {
            return false;
        }

        return appointmentDate.isAfter(LocalDateTime.now());
    }
}
