package com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.predicates;


import com.querydsl.core.BooleanBuilder;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.QTreatmentEntity;

public class TreatmentPredicateBuilder{

  public  static BooleanBuilder buildTreatmentFilterExpression(Double dosis, String unit, String description, String medicalAppointmentId){
    var qTreatmentEntity = QTreatmentEntity.treatmentEntity;
    var expressionBuilder = new BooleanBuilder();

    if(dosis != null){
      expressionBuilder.or(qTreatmentEntity.dosis.eq(dosis));
    }
    if(unit != null){
      expressionBuilder.or(qTreatmentEntity.unit.containsIgnoreCase(unit));
    }
    if(description != null){
      expressionBuilder.or(qTreatmentEntity.description.containsIgnoreCase(description));
    }
    if(medicalAppointmentId != null){
      expressionBuilder.and(qTreatmentEntity.medicalAppointment.id.eq(medicalAppointmentId));
    }
    expressionBuilder.and(qTreatmentEntity.medicalAppointment.isNotNull());
    return expressionBuilder;
  }
}
