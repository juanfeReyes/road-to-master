package com.road.master.PetShelter.infrastructure.persistence.medicalAppointment;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository extends CrudRepository<TreatmentEntity, String>,
    QuerydslPredicateExecutor<TreatmentEntity> {


  @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "complete_treatment")
  TreatmentEntity getTreatmentById(String id);
}
