package com.road.master.PetShelter.infrastructure.persistence.user;

import com.road.master.PetShelter.domain.user.Doctor;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctor")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DoctorEntity {

  @Id
  private String id;

  @Column
  private String name;

  @Column
  private String lastname;

  @Column
  private String expertise;

  public static DoctorEntity build(Doctor doctor) {
    return new DoctorEntity(doctor.getId(), doctor.getName(), doctor.getLastname(), doctor.getExpertise());
  }

  public Doctor toDomain() {
    return new Doctor(id, name, lastname, expertise);
  }
}
