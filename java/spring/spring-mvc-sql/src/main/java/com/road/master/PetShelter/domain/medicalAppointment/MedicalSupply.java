package com.road.master.PetShelter.domain.medicalAppointment;

import lombok.Data;


import java.time.LocalDate;

@Data
public class MedicalSupply {
  private String id;

  private double content;

  private String contentUnit;

  private LocalDate expirationDate;
}
