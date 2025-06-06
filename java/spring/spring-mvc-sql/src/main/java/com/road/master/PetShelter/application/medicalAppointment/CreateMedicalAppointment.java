package com.road.master.PetShelter.application.medicalAppointment;

import com.road.master.PetShelter.domain.exceptions.ConflictException;
import com.road.master.PetShelter.domain.exceptions.NotFoundException;
import com.road.master.PetShelter.domain.medicalAppointment.MedicalAppointment;
import com.road.master.PetShelter.infrastructure.api.medicalAppointment.dto.MedicalAppointmentRequest;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.IMedicalAppointmentRepository;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.MedicalAppointmentEntity;
import com.road.master.PetShelter.infrastructure.persistence.pet.IPetRepository;
import com.road.master.PetShelter.infrastructure.persistence.user.IDoctorRepository;
import com.road.master.PetShelter.infrastructure.services.EmailService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CreateMedicalAppointment {

  private final IMedicalAppointmentRepository medicalAppointmentRepository;

  private final IPetRepository petRepository;

  private final IDoctorRepository doctorRepository;

  private final EmailService emailService;

  @Autowired
  public CreateMedicalAppointment(IMedicalAppointmentRepository medicalAppointmentRepository,
                                  IDoctorRepository doctorRepository,
                                  IPetRepository petRepository, EmailService emailService) {
    this.medicalAppointmentRepository = medicalAppointmentRepository;
    this.petRepository = petRepository;
    this.doctorRepository = doctorRepository;
    this.emailService = emailService;
  }

  @Transactional
  public MedicalAppointment execute(MedicalAppointmentRequest request) {
    var pet = petRepository.findById(request.getPetId());
    if (pet.isEmpty()) {
      throw new NotFoundException("pet not found with id: " + request.getPetId());
    }

    var doctor = doctorRepository.findById(request.getDoctorId());
    if (doctor.isEmpty()) {
      throw new NotFoundException("doctor not found with id: " + request.getDoctorId());
    }

    var savedAppointment = medicalAppointmentRepository.findById(request.getId());
    if (savedAppointment.isPresent()) {
      throw new ConflictException("Medical appointment already exists");
    }

    var medicalAppointment = medicalAppointmentRepository.save(
        new MedicalAppointmentEntity(request.getId(),
            request.getScheduleDate(),
            request.getAtentionDate(),
            request.getDescription(),
            pet.get(),
            doctor.get()));

    notifyEmails();
    return medicalAppointment.toDomain();
  }

  public void notifyEmails() {
    try {
      var doctorEmail = emailService.sendEmail();
      var assistantEmail = emailService.sendEmail();

      var allEmailsCompleted = CompletableFuture.allOf(doctorEmail, assistantEmail);
      allEmailsCompleted.get();
    }catch (Exception e){
      // log the emails were not sent due the exception error
    }
  }

}
