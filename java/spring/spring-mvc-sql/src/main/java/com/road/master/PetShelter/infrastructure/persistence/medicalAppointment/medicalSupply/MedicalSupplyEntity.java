package com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.medicalSupply;

import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medical_supply")
public class MedicalSupplyEntity {

  @Id
  private String id;

  @Column
  private String reference;

  @Column
  private double content;

  @Column
  private String contentUnit;

  @Column
  private LocalDate expirationDate;

  @ManyToOne
  private TreatmentEntity treatment;
}
