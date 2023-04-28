package com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.medicalSupply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalSupplyRepository extends JpaRepository<MedicalSupplyEntity, String>, ICustomMedicalSupplyRepository {
}
