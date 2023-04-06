package com.road.master.PetShelter.application.medicalAppointment;

import com.road.master.PetShelter.domain.medicalAppointment.MedicalAppointment;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.IMedicalAppointmentRepository;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.MedicalAppointmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class GetMedicalAppointments {

  private final IMedicalAppointmentRepository medicalAppointmentRepository;

  @Autowired
  public GetMedicalAppointments(IMedicalAppointmentRepository medicalAppointmentRepository) {
    this.medicalAppointmentRepository = medicalAppointmentRepository;
  }

  public List<MedicalAppointment> execute(){
    return StreamSupport.stream(medicalAppointmentRepository.findAll().spliterator(), false)
            .map(MedicalAppointmentEntity::toDomain)
            .toList();
  }
}
