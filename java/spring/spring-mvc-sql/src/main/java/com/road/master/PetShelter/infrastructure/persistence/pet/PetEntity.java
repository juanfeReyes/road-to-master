package com.road.master.PetShelter.infrastructure.persistence.pet;

import com.road.master.PetShelter.domain.pet.Pet;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Table(name = "pet")
public class PetEntity {

  @Id
  private String id;

  @Column
  private String name;

  @Column
  private String race;

  @Column
  private boolean active;

  public PetEntity() {
  }

  public PetEntity(String id, String name, String race) {
    this.id = id;
    this.name = name;
    this.race = race;
  }

  public static PetEntity toEntity(Pet pet) {
    return new PetEntity(
        pet.getId(),
        pet.getName().getValue(),
        pet.getRace().getValue());
  }

  public static Pet toPet(PetEntity entity) {
    return new Pet(entity.id, entity.name, entity.race);
  }
}
