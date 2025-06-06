package com.road.master.PetShelter.infrastructure.mapper;

import com.road.master.PetShelter.domain.medicalAppointment.MedicalSupply;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.medicalSupply.MedicalSupplyEntity;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface MedicalSupplyMapper {

  Set<MedicalSupplyEntity> toEntityList(Set<MedicalSupply> medicalSupply);

  Set<MedicalSupply> toDomainList(Set<MedicalSupplyEntity> medicalSupply);


}
