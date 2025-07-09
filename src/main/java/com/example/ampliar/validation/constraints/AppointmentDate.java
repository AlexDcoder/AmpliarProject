package com.example.ampliar.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import com.example.ampliar.validation.AppointmentDateValidator;

@Documented
@Constraint(validatedBy = AppointmentDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppointmentDate {
    String message() default "Data de agendamento inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
