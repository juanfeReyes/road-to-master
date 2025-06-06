package com.road.master.PetShelter.application.medicalAppointment;

import com.road.master.PetShelter.domain.exceptions.NotFoundException;
import com.road.master.PetShelter.domain.medicalAppointment.Treatment;
import com.road.master.PetShelter.infrastructure.mapper.TreatmentMapper;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.IMedicalAppointmentRepository;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentRepository;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreateTreatment {

  private final TreatmentRepository treatmentRepository;

  private final IMedicalAppointmentRepository medicalAppointmentRepository;

  private final TreatmentMapper treatmentMapper;

  @Autowired
  public CreateTreatment(TreatmentRepository treatmentRepository,
                         IMedicalAppointmentRepository medicalAppointmentRepository, TreatmentMapper treatmentMapper) {
    this.treatmentRepository = treatmentRepository;
    this.medicalAppointmentRepository = medicalAppointmentRepository;
    this.treatmentMapper = treatmentMapper;
  }

  @Transactional
  public List<Treatment> execute(String appointmentId, List<Treatment> treatments) {
    //Verify medical appointment exists
    var medicalAppointment = medicalAppointmentRepository.findById(appointmentId);
    if (medicalAppointment.isEmpty()) {
      throw new NotFoundException("The medical appointment with id: " + appointmentId + " does not exists");
    }

    //Build treatment
    treatments.forEach(treatment -> treatment.setMedicalAppointment(medicalAppointment.get().toDomain()));
    var treatmentEntities = treatments.stream().map(t -> treatmentMapper.toEntity(t, null))
        .collect(Collectors.toList());
    //Save Treatment
    treatmentRepository.saveAll(treatmentEntities);

    return treatments;
  }
}
