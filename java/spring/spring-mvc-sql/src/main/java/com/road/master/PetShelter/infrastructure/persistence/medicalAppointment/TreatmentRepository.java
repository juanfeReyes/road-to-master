package com.road.master.PetShelter.infrastructure.persistence.medicalAppointment;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface TreatmentRepository extends CrudRepository<TreatmentEntity, String>,
    QuerydslPredicateExecutor<TreatmentEntity> {


  @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "complete_treatment")
  @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
  TreatmentEntity getTreatmentById(String id);
}
