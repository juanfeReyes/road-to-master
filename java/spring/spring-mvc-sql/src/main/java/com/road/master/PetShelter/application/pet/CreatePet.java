package com.road.master.PetShelter.application.pet;

import com.road.master.PetShelter.domain.exceptions.ConflictException;
import com.road.master.PetShelter.domain.pet.Pet;
import com.road.master.PetShelter.domain.pet.PetResource;
import com.road.master.PetShelter.infrastructure.messaging.pet.PetResourceSender;
import com.road.master.PetShelter.infrastructure.persistence.pet.IPetRepository;
import com.road.master.PetShelter.infrastructure.persistence.pet.PetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreatePet {

  private final PetResourceSender petResourceSender;

  private final IPetRepository petRepository;

  @Autowired
  public CreatePet(IPetRepository petRepository,
                   PetResourceSender petResourceSender) {
    this.petRepository = petRepository;
    this.petResourceSender = petResourceSender;
  }

  @Transactional
  public Pet execute(String id, String name, String race) {
    // Verify if exits else throe already exist exception
    var existingPet = petRepository.findById(id);
    if (existingPet.isPresent()) {
      //Throw exception
      throw new ConflictException("Pet with id " + id + " is already created");
    }

    var newPet = new Pet(id, name, race);
    var createdPet = PetEntity.toPet(petRepository.save(PetEntity.toEntity(newPet)));
    //save pet in BD and return it

    var petResource = PetResource.buildHealthPack("MEDIUM", createdPet);
    petResourceSender.send(petResource);

    return createdPet;
  }
}
