package com.example.ampliar.validation.constraints;

import com.example.ampliar.validation.BirthDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDate {
    String message() default "Data de nascimento inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

