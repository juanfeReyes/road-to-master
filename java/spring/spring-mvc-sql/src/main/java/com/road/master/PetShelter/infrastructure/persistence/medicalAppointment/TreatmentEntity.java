package com.road.master.PetShelter.infrastructure.persistence.medicalAppointment;

import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.medicalSupply.MedicalSupplyEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
  private String reference;

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

  @OneToMany(cascade = CascadeType.ALL)
  private Set<MedicalSupplyEntity> medicalSupplyEntities;


}
