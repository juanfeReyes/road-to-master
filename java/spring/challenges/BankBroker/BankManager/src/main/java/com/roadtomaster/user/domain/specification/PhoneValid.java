package com.roadtomaster.user.domain.specification;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneValid {
  String message() default "Phone is invalid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
