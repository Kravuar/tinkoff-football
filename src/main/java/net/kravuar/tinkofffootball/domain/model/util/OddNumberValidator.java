package net.kravuar.tinkofffootball.domain.model.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OddNumberValidator implements ConstraintValidator<OddNumber, Integer> {
    @Override
    public void initialize(OddNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer number, ConstraintValidatorContext context) {
        return number % 2 != 0;
    }
}
