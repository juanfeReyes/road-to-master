package com.road.master.PetShelter.application.medicalAppointment;

import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentEntity;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentRepository;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.predicates.TreatmentPredicateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GetTreatmentByFilters {

  private final TreatmentRepository treatmentRepository;

  @Autowired
  public GetTreatmentByFilters(TreatmentRepository treatmentRepository) {
    this.treatmentRepository = treatmentRepository;
  }

  public Page<String> execute(Double dosis, String unit, String description, String medicalAppointmentId, PageRequest pageable) {
    var searchParams = TreatmentPredicateBuilder.buildTreatmentFilterExpression(dosis, unit, description, medicalAppointmentId);
    var entities = treatmentRepository.findAll(searchParams, pageable);

    return entities.map(TreatmentEntity::getId);
  }
}
