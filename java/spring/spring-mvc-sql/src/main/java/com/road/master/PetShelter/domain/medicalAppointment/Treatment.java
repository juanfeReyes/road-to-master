package com.road.master.PetShelter.domain.medicalAppointment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class Treatment {

  private String id;

  private double dosis;

  private String unit;

  private String description;

  private MedicalAppointment medicalAppointment;

  @JsonBackReference
  private Treatment treatmentGroup;

  @JsonManagedReference
  private List<Treatment> treatments;

  private Set<MedicalSupply> medicalSupplyEntities;

}
