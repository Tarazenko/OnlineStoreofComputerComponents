package com.nc.annotation;

import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckPriceValidator implements ConstraintValidator<CheckPrice, String> {
    private final static Logger LOGGER = Logger.getLogger(CheckPriceValidator.class);

    @Override
    public boolean isValid(String price, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid;
        double priceDouble = Double.parseDouble(price);
        LOGGER.info("Checking the field for negative values.");
        isValid = priceDouble > 0;
        return isValid;
    }
}
