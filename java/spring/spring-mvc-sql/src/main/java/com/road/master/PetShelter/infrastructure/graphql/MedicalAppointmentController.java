package com.road.master.PetShelter.infrastructure.graphql;

import com.road.master.PetShelter.application.medicalAppointment.CreateMedicalAppointment;
import com.road.master.PetShelter.domain.medicalAppointment.MedicalAppointment;
import com.road.master.PetShelter.infrastructure.api.medicalAppointment.dto.MedicalAppointmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MedicalAppointmentController {

  private final CreateMedicalAppointment createMedicalAppointment;

  @Autowired
  public MedicalAppointmentController(CreateMedicalAppointment createMedicalAppointment) {
    this.createMedicalAppointment = createMedicalAppointment;
  }

  @MutationMapping
  public MedicalAppointment createMedicalAppointment(@Argument MedicalAppointmentRequest medicalAppointment) {
    return createMedicalAppointment.execute(medicalAppointment);
  }
}
