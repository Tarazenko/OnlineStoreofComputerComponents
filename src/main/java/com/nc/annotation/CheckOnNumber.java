package com.nc.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckOnNumberValidator.class)
public @interface CheckOnNumber {
    String message() default "Invalid field.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
