package com.road.master.PetShelter.infrastructure.persistence.medicalAppointment;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMedicalAppointmentRepository extends CrudRepository<MedicalAppointmentEntity, String>,
        QuerydslPredicateExecutor<MedicalAppointmentEntity> {
}
