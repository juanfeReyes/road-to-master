package com.roadtomaster.user.domain.specification;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidValidator implements ConstraintValidator<PhoneValid, String> {
  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
    return phoneNumber != null && phoneNumber.length() >= 7 && phoneNumber.length() <= 10;
  }
}
