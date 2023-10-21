package net.kravuar.tinkofffootball.domain.model.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OddNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OddNumber {
    String message() default "Должно быть нечётным.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

