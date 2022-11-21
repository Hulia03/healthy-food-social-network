package com.telerikacademy.healthy.food.social.network.annotations.validators;

import com.telerikacademy.healthy.food.social.network.annotations.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {
    private Pattern pattern;

    private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

    @Override
    public void initialize(Password arg0) {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}