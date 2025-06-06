package com.road.master.PetShelter.application.pet;

import com.road.master.PetShelter.domain.pet.Pet;
import com.road.master.PetShelter.infrastructure.persistence.pet.IPetRepository;
import com.road.master.PetShelter.infrastructure.persistence.pet.PetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GetPets {

  private final IPetRepository petRepository;

  @Autowired
  public GetPets(IPetRepository petRepository) {
    this.petRepository = petRepository;
  }

  @Transactional
  public List<Pet> execute() {
    var pets = StreamSupport.stream(petRepository.findAll().spliterator(), false)
        .map(pet -> PetEntity.toPet(pet))
        .collect(Collectors.toList());

    return pets;
  }
}
