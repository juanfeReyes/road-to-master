package com.road.master.PetShelter.application.user;

import com.road.master.PetShelter.domain.exceptions.ConflictException;
import com.road.master.PetShelter.domain.user.Doctor;
import com.road.master.PetShelter.infrastructure.persistence.user.DoctorEntity;
import com.road.master.PetShelter.infrastructure.persistence.user.IDoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateDoctor {

  private final IDoctorRepository doctorRepository;

  @Autowired
  public CreateDoctor(IDoctorRepository doctorRepository) {
    this.doctorRepository = doctorRepository;
  }

  @Transactional
  public Doctor execute(Doctor doctor) {
    var savedDoctor = doctorRepository.findById(doctor.getId());
    if (savedDoctor.isPresent()) {
      throw new ConflictException("Doctor already exists with id: " + doctor.getId());
    }

    doctorRepository.save(DoctorEntity.build(doctor));
    return doctor;
  }
}
