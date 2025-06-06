package com.road.master.PetShelter.domain.pet;

import schemas.Donation.Resource;
import schemas.Pet.PetResourceRequest;
import schemas.Pet.PriorityEnum;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class PetResource {

  public static PetResourceRequest buildHealthPack(String priority, Pet pet) {
    var resources = List.of(
        Resource.newBuilder()
            .setName("alcohol")
            .setId(UUID.randomUUID().toString())
            .setAmount(1).build(),
        Resource.newBuilder()
            .setName("antibiotics")
            .setId(UUID.randomUUID().toString())
            .setAmount(10).build(),
        Resource.newBuilder()
            .setName("vitamins")
            .setId(UUID.randomUUID().toString())
            .setAmount(5).build()
    );

    var petResource = new PetResourceRequest();
    petResource.setPriority(PriorityEnum.valueOf(priority));
    petResource.setRace(pet.getRace().getValue());
    petResource.setName(pet.getName().getValue());
    petResource.setId(pet.getId());
    petResource.setCreationDate(ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
    petResource.setResources(resources);
    return petResource;
  }
}
