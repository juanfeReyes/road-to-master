package com.road.master.PetShelter.infrastructure.persistence;

import com.road.master.PetShelter.BaseTestConfiguration;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentEntity;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentRepository;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.medicalSupply.MedicalSupplyEntity;
import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.medicalSupply.MedicalSupplyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomMedicalSupplyRepositoryTest extends BaseTestConfiguration {

  @Autowired
  private MedicalSupplyRepository medicalSupplyRepository;

  @Autowired
  private TreatmentRepository treatmentRepository;

  @Before
  public void setup(){
    var treatment = TreatmentEntity.builder().id(UUID.randomUUID().toString()).reference("Amlodipino").build();
    treatmentRepository.save(treatment);
    medicalSupplyRepository.save(MedicalSupplyEntity.builder()
            .id(UUID.randomUUID().toString()).reference("Amlodipino")
            .expirationDate(LocalDate.now().minus(1, ChronoUnit.DAYS))
            .build());
    medicalSupplyRepository.save(MedicalSupplyEntity.builder()
            .id(UUID.randomUUID().toString()).reference("Ibuprofeno")
            .build());
    medicalSupplyRepository.save(MedicalSupplyEntity.builder()
            .id(UUID.randomUUID().toString()).reference("Amlodipino")
            .expirationDate(LocalDate.now().plus(1, ChronoUnit.DAYS))
            .build());
    medicalSupplyRepository.save(MedicalSupplyEntity.builder()
            .id(UUID.randomUUID().toString()).reference("Amlodipino")
            .expirationDate(LocalDate.now().plus(1, ChronoUnit.DAYS))
            .treatment(treatment)
            .build());
  }

  @Test
  public void shouldGetMedicalSupplyForTreatment(){
    var medicalSupplies = medicalSupplyRepository.getMedicalSupplyForTreatment("Amlodipino");

    assertThat(medicalSupplies).hasSize(1);
  }


  @Test
  public void shouldCreateMedicalSupply(){
    var treatment = TreatmentEntity.builder().id(UUID.randomUUID().toString()).reference("Amlodipino").build();
    var medicalSupply = MedicalSupplyEntity.builder()
            .id(UUID.randomUUID().toString()).reference("Amlodipino")
            .expirationDate(LocalDate.now().plus(1, ChronoUnit.DAYS))
            .treatment(treatment)
            .build();
    medicalSupplyRepository.saveMedicalSupplyForTreatment(medicalSupply);

    var savedMedicalSupply = medicalSupplyRepository.findById(medicalSupply.getId());
    assertThat(savedMedicalSupply).isNotNull();
  }
}
