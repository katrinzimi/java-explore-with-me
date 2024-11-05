package ru.practicum.explorewithme.server;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckEventDateValidator.class)
@Documented
public @interface CheckEventDate {

    String message() default "{message.key}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}