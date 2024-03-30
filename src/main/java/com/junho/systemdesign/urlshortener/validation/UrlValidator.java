package com.junho.systemdesign.urlshortener.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UrlValidator implements ConstraintValidator<ValidUrl, String> {

    private static final String URL_PATTERN = "((http(s)?)://(www\\.)?|www\\.)[a-zA-Z0-9@:%._\\+~#=-]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";

    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return url.matches(URL_PATTERN);
    }

}
