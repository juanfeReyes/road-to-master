package com.road.master.PetShelter.domain.exceptions;

public class MissingArgumentException extends RuntimeException {

  public MissingArgumentException(String argument) {
    super(String.format("Missing %s argument", argument));
  }
}
