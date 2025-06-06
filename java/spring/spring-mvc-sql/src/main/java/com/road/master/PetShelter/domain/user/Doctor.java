package com.road.master.PetShelter.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Doctor {

  private String id;

  private String name;

  private String lastname;

  private String expertise;
}
