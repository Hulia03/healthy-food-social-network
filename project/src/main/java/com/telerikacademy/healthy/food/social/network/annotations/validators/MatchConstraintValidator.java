package com.telerikacademy.healthy.food.social.network.annotations.validators;

import com.telerikacademy.healthy.food.social.network.annotations.Match;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchConstraintValidator implements ConstraintValidator<Match, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final Match constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid;
        try {
            final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(value, secondFieldName);

            valid = firstObj == null && secondObj == null ||
                    firstObj != null && firstObj.equals(secondObj);
        } catch (final Exception ignore) {
            return false;
        }

        return valid;
    }
}