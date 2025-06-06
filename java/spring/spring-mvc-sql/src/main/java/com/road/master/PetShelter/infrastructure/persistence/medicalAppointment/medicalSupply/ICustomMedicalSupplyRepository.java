package com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.medicalSupply;

import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentEntity;

import java.util.Set;

public interface ICustomMedicalSupplyRepository {
  Set<MedicalSupplyEntity> getMedicalSupplyForTreatment(String reference);

  MedicalSupplyEntity saveMedicalSupplyForTreatment(MedicalSupplyEntity medicalSupplyEntity);
}
