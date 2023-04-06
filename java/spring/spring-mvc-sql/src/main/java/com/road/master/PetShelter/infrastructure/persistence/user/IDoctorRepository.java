package com.road.master.PetShelter.infrastructure.persistence.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDoctorRepository extends CrudRepository<DoctorEntity, String> {
}
