package com.road.master.PetShelter.domain.pet;

import com.road.master.PetShelter.domain.valueObjects.Name;
import com.road.master.PetShelter.domain.valueObjects.Race;
import lombok.Getter;

@Getter
public class Pet {

  private final String id;

  private final Name name;

  private final Race race;

  public Pet(String id, String name, String race) {
    this.id = id;
    this.name = new Name(name);
    this.race = new Race(race);
  }
}
