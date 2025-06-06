package com.road.master.PetShelter.application.medicalAppointment;

import com.road.master.PetShelter.domain.medicalAppointment.Treatment;
import com.road.master.PetShelter.infrastructure.mapper.TreatmentMapper;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentEntity;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTreatmentById {

  private final TreatmentRepository treatmentRepository;

  private final TreatmentMapper treatmentMapper;

  @Autowired
  public GetTreatmentById(TreatmentRepository treatmentRepository, TreatmentMapper treatmentMapper) {
    this.treatmentRepository = treatmentRepository;
    this.treatmentMapper = treatmentMapper;
  }

  public Treatment execute(String id) {
    var treatmentEntity = treatmentRepository.getTreatmentById(id);
    return treatmentMapper.toDomain(treatmentEntity, null);
  }
}
