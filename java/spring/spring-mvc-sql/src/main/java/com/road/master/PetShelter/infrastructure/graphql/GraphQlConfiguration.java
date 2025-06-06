package com.road.master.PetShelter.infrastructure.graphql;

import com.road.master.PetShelter.infrastructure.persistence.medicalAppointment.IMedicalAppointmentRepository;
import graphql.scalars.ExtendedScalars;
import graphql.schema.idl.TypeRuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.data.query.QuerydslDataFetcher;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQlConfiguration {

  @Autowired
  private IMedicalAppointmentRepository medicalAppointmentRepository;

  @Bean
  public RuntimeWiringConfigurer runtimeWiringConfigurer() {
    return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.Date);
  }

  @Bean
  public RuntimeWiringConfigurer medicalAppointmentConfig() {
    var dataFetcher = QuerydslDataFetcher.builder(medicalAppointmentRepository).many();
    return wiringBuilder -> wiringBuilder
            .type(TypeRuntimeWiring.newTypeWiring("Query").dataFetcher("getMedicalAppointments", dataFetcher));
  }
}
