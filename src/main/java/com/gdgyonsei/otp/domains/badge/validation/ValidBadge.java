package com.gdgyonsei.otp.domains.badge.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BadgeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBadge {
    String message() default "Invalid badge configuration";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
