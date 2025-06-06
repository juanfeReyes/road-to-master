package com.road.master.PetShelter.infrastructure.persistence.pet;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPetRepository extends CrudRepository<PetEntity, String> {

  @Query(value = "CALL report(:race, :expertise, :adopterAddress, :adopterName);", nativeQuery = true)
  List<PetEntity> report(@Param("race") String race,
                         @Param("expertise") String expertise,
                         @Param("adopterAddress") String adopterAddress,
                         @Param("adopterName") String adopterName);
}
