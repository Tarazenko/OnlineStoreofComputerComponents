package com.nc.annotation;

import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

public class CheckOnHtmlValidator implements ConstraintValidator<CheckOnHtml, String> {
    private final static Logger LOGGER = Logger.getLogger(CheckOnHtmlValidator.class);

    @Override
    public boolean isValid(String startLine, ConstraintValidatorContext constraintValidatorContext) {
        String correctLine = escapeHtml4(startLine);
        LOGGER.info("Check for the absence of html in the field.");
        boolean isValid;
        isValid = correctLine.equals(startLine);
        return isValid;
    }
}
