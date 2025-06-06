package com.road.master.PetShelter.infrastructure.api.medicalAppointment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreatmentFilters {

  private Double dosis;

  private String unit;

  private String description;

  private String medicalAppointmentId;
}
