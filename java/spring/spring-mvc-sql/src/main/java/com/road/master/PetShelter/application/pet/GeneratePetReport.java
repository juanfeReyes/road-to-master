package com.road.master.PetShelter.application.pet;

import com.road.master.PetShelter.domain.pet.Pet;
import com.road.master.PetShelter.infrastructure.persistence.pet.IPetRepository;
import com.road.master.PetShelter.infrastructure.persistence.pet.PetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneratePetReport {

  private final IPetRepository petRepository;

  @Autowired
  public GeneratePetReport(IPetRepository petRepository) {
    this.petRepository = petRepository;
  }

  @Transactional
  public List<Pet> execute(String petRace, String doctorExpertise, String adopterAddress, String adopterName) {

    var entities = petRepository.report(petRace, doctorExpertise, adopterAddress, adopterName);
    return entities.stream()
        .map(PetEntity::toPet)
        .collect(Collectors.toList());
  }
}
