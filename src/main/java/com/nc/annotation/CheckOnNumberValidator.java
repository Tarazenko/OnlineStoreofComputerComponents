package com.nc.annotation;

import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckOnNumberValidator implements ConstraintValidator<CheckOnNumber, String> {
    private final static Logger LOGGER = Logger.getLogger(CheckOnNumberValidator.class);

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid;
        LOGGER.info("Проверка на отсутсвие чисел в поле.");
        isValid = !name.matches(".*[0-9].*");
        return isValid;
    }
}
