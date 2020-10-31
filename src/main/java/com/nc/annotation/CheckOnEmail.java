package com.nc.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Pattern(regexp = "^[-a-z0-9_-]+(\\.[-a-z0-9_-]+)*@([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*([a-z][a-z])$")
@ReportAsSingleViolation
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface CheckOnEmail {
    String message()
            default "Invalid mail";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
