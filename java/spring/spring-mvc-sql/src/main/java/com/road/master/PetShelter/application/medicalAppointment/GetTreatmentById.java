package com.road.master.PetShelter.application.medicalAppointment;

import com.road.master.PetShelter.domain.medicalAppointment.Treatment;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentEntity;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTreatmentById {

  private final TreatmentRepository treatmentRepository;

  @Autowired
  public GetTreatmentById(TreatmentRepository treatmentRepository) {
    this.treatmentRepository = treatmentRepository;
  }

  public Treatment execute(String id) {
    var treatmentEntity = treatmentRepository.getTreatmentById(id);
    return TreatmentEntity.toDomain(treatmentEntity, null);
  }
}
