package com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.medicalSupply;

import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.TreatmentEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.HashSet;
import java.util.Set;

public class MedicalSupplyRepositoryImpl implements ICustomMedicalSupplyRepository{

  @PersistenceContext
  private EntityManager entityManager;

  @PersistenceUnit
  private EntityManagerFactory entityManagerFactory;

  @Override
  public Set<MedicalSupplyEntity> getMedicalSupplyForTreatment(String reference) {
    var query = """
            select medical_supply from MedicalSupplyEntity medical_supply
            where medical_supply.reference = :reference
              and medical_supply.treatment = NULL
              and medical_supply.expirationDate >= NOW()
            """;
    return new HashSet<>(entityManager.createQuery(query, MedicalSupplyEntity.class)
            .setParameter("reference", reference)
            .getResultList());
  }

  @Override
  public MedicalSupplyEntity saveMedicalSupplyForTreatment(MedicalSupplyEntity medicalSupplyEntity) {
    // For transaction is required to create a new entity manager
    var em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();

    em.persist(medicalSupplyEntity);
    em.persist(medicalSupplyEntity.getTreatment());
    em.getTransaction().commit();
    return medicalSupplyEntity;
  }
}
