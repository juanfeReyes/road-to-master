package com.road.master.PetShelter.infrastructure.persistence.medicalAppointment;

import com.road.master.PetShelter.domain.medicalAppointment.MedicalAppointment;
import com.road.master.PetShelter.infrastructure.persistence.pet.PetEntity;
import com.road.master.PetShelter.infrastructure.persistence.user.DoctorEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "medical_appointment")
public class MedicalAppointmentEntity {

  @Id
  private String id;

  @Column
  private LocalDate scheduleDate;

  @Column
  private LocalDate atentionDate;

  @Column
  private String description;

  @ManyToOne
  @JoinColumn(name = "pet_id",
      foreignKey = @ForeignKey(name = "fk_medical_appointment_pet")
  )
  private PetEntity pet;

  @ManyToOne
  @JoinColumn(name = "doctor_id",
      foreignKey = @ForeignKey(name = "fk_medical_appointment_doctor")
  )
  private DoctorEntity doctor;

  public static MedicalAppointmentEntity build(MedicalAppointment medicalAppointment) {
    return new MedicalAppointmentEntity(
        medicalAppointment.getId(),
        medicalAppointment.getScheduleDate(),
        medicalAppointment.getAtentionDate(),
        medicalAppointment.getDescription(),
        PetEntity.toEntity(medicalAppointment.getPet()),
        DoctorEntity.build(medicalAppointment.getDoctor()));
  }

  public MedicalAppointment toDomain() {
    return new MedicalAppointment(id, scheduleDate, atentionDate, description,
        doctor.toDomain(), PetEntity.toPet(pet));
  }
}
