package com.nnk.springboot.config.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for Password validation
 */

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    /**
     * Check if password is valid
     * @param password password to validate
     * @param constraintValidatorContext context of validation
     * @return true if password is valid
     */

    // digit + lowercase char + uppercase char + punctuation + symbol
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void initialize(ValidPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(final String password, ConstraintValidatorContext constraintValidatorContext) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
