package com.sales.backend.SalesBackend.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidatore implements ConstraintValidator<ImageNameValid, String> {

    private final Logger logger = LoggerFactory.getLogger(ImageNameValidatore.class);

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        logger.info("message from ImageNameValidatore : {}", s);

        if (s == null || s.isBlank()) {
            return true;
        }

        return s.matches(".*\\.(jpg|jpeg|png|webp)$");
    }
}