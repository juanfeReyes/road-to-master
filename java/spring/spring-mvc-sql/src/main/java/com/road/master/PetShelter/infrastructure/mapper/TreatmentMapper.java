package com.road.master.PetShelter.infrastructure.mapper;

import com.road.master.PetShelter.domain.medicalAppointment.MedicalAppointment;
import com.road.master.PetShelter.domain.medicalAppointment.Treatment;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.MedicalAppointmentEntity;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
  uses = {MedicalSupplyMapper.class})
public abstract class TreatmentMapper {

  @Autowired
  private MedicalSupplyMapper medicalSupplyMapper;

  public TreatmentEntity toEntity(Treatment treatment, TreatmentEntity treatmentGroupEntity) {
    var medicalAppointmentEntity = treatment.getMedicalAppointment() != null ? MedicalAppointmentEntity.build(treatment.getMedicalAppointment()) : null;
    var medicalSupplies = medicalSupplyMapper.toEntityList(treatment.getMedicalSupplyEntities());
    var entity = TreatmentEntity.builder()
            .id(treatment.getId())
            .dosis(treatment.getDosis())
            .unit(treatment.getUnit())
            .description(treatment.getDescription())
            .medicalAppointment(medicalAppointmentEntity)
            .treatmentGroup(treatmentGroupEntity)
            .medicalSupplyEntities(medicalSupplies)
            .build();

    var treatmentEntities = treatment.getTreatments().stream()
            .map(t -> toEntity(t, entity))
            .collect(Collectors.toSet());
    entity.setTreatments(treatmentEntities);
    return entity;
  }

  public Treatment toDomain(TreatmentEntity treatmentEntity, Treatment treatmentGroup) {
    var medicalAppointment = treatmentEntity.getMedicalAppointment() != null ?
            treatmentEntity.getMedicalAppointment().toDomain() : null;
    var medicalSupplies = medicalSupplyMapper.toDomainList(treatmentEntity.getMedicalSupplyEntities());
    var treatment = new Treatment(treatmentEntity.getId(),
            treatmentEntity.getDosis(),
            treatmentEntity.getUnit(),
            treatmentEntity.getDescription(),
            medicalAppointment,
            treatmentGroup,
            null,
            medicalSupplies);

    var treatments = treatmentEntity.getTreatments().stream()
            .map(e -> toDomain(e, treatment)).collect(Collectors.toList());
    treatment.setTreatments(treatments);
    return treatment;
  }


}
