package com.road.master.PetShelter.infrastructure.api.medicalAppointment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.road.master.PetShelter.domain.medicalAppointment.Treatment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class TreatmentRequest {

  @JsonProperty(value = "appointmentId")
  private String medicalAppointmentId;

  @JsonProperty(value = "treatment_list")
  private List<TreatmentItem> treatments;

  public static List<Treatment> treatmentItemsToDomain(List<TreatmentItem> treatmentItems, Treatment treatmentGroup) {
    if (treatmentItems.isEmpty()) {
      return Collections.emptyList();
    }
    return treatmentItems.stream().map(treatmentItem -> {
          var t = new Treatment(
              treatmentItem.getId() != null ? treatmentItem.getId() : UUID.randomUUID().toString(),
              treatmentItem.getDosis(),
              treatmentItem.getUnit(),
              treatmentItem.getDescription(),
              null,
              treatmentGroup,
              null);
          t.setTreatments(treatmentItemsToDomain(treatmentItem.getTreatments(), t));
          return t;
        })
        .collect(Collectors.toList());
  }
}

@Getter
@Setter
class TreatmentItem {

  @JsonProperty(value = "id")
  private String id;

  @JsonProperty(value = "dosis")
  private double dosis;

  @JsonProperty(value = "unit")
  private String unit;

  @JsonProperty(value = "description")
  private String description;

  @JsonProperty(value = "treatments")
  private List<TreatmentItem> treatments;

}
