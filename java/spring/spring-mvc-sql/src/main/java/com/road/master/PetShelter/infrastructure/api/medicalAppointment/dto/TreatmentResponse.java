package com.road.master.PetShelter.infrastructure.api.medicalAppointment.dto;

import com.road.master.PetShelter.domain.medicalAppointment.Treatment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentResponse {

  private String id;

  private double dosis;

  private String unit;

  private String description;

  private String medicalAppointmentId;

  private List<TreatmentResponse> treatments;

  public static TreatmentResponse toResponse(Treatment treatment) {
    var treatments = treatment.getTreatments().stream()
        .map(TreatmentResponse::toResponse).collect(Collectors.toList());
    return new TreatmentResponse(
        treatment.getId(),
        treatment.getDosis(),
        treatment.getUnit(),
        treatment.getDescription(),
        treatment.getMedicalAppointment().getId(),
        treatments
    );
  }
}
