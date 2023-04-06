package com.road.master.PetShelter.infrastructure.persistence.medicalAppointment;

import com.road.master.PetShelter.domain.medicalAppointment.Treatment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "treatment")
@NamedEntityGraph(name = "complete_treatment", attributeNodes = {
        @NamedAttributeNode("treatmentGroup"),
        @NamedAttributeNode("treatments")
})
public class TreatmentEntity {

  @Id
  private String id;

  @Column
  private double dosis;

  @Column
  private String unit;

  @Column
  private String description;

  @ManyToOne
  @JoinColumn(name = "medical_appointment_id",
      foreignKey = @ForeignKey(name = "fk_treatment_medical_appointment")
  )
  private MedicalAppointmentEntity medicalAppointment;

  @ManyToOne
  @JoinColumn(name = "treatment_group",
      foreignKey = @ForeignKey(name = "fk_treatment_group"))
  private TreatmentEntity treatmentGroup;

  @OneToMany(mappedBy = "treatmentGroup", cascade = CascadeType.ALL)
  private Set<TreatmentEntity> treatments;

  public static TreatmentEntity toEntity(Treatment treatment, TreatmentEntity treatmentGroupEntity) {
    var medicalAppointmentEntity = treatment.getMedicalAppointment() != null ? MedicalAppointmentEntity.build(treatment.getMedicalAppointment()) : null;
    var entity = new TreatmentEntity(treatment.getId(),
        treatment.getDosis(),
        treatment.getUnit(),
        treatment.getDescription(),
        medicalAppointmentEntity,
        treatmentGroupEntity,
        null);
    var treatmentEntities = treatment.getTreatments().stream()
        .map(t -> TreatmentEntity.toEntity(t, entity))
        .collect(Collectors.toSet());
    entity.setTreatments(treatmentEntities);
    return entity;
  }

  public static Treatment toDomain(TreatmentEntity treatmentEntity, Treatment treatmentGroup) {
    var medicalAppointment = treatmentEntity.getMedicalAppointment() != null ?
        treatmentEntity.getMedicalAppointment().toDomain() : null;

    var treatment = new Treatment(treatmentEntity.getId(),
        treatmentEntity.getDosis(),
        treatmentEntity.getUnit(),
        treatmentEntity.getDescription(),
        medicalAppointment,
        treatmentGroup,
        null);

    var treatments = treatmentEntity.getTreatments().stream()
        .map(e -> TreatmentEntity.toDomain(e, treatment)).collect(Collectors.toList());
    treatment.setTreatments(treatments);
    return treatment;
  }
}
