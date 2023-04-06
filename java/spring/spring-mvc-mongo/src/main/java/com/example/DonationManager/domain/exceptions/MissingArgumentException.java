package com.example.DonationManager.domain.exceptions;

public class MissingArgumentException extends RuntimeException {

  public MissingArgumentException(String argument) {
    super(String.format("Missing %s argument", argument));
  }
}
