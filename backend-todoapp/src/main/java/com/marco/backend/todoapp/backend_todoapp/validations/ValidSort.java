package com.marco.backend.todoapp.backend_todoapp.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SortValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSort {
    String message() default "Invalid sort value. Must be one of: ASC, DESC.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}