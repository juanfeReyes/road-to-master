package com.road.master.PetShelter;

import com.road.master.PetShelter.domain.medicalAppointment.MedicalAppointment;
import com.road.master.PetShelter.infrastructure.persistence.pet.IPetRepository;
import com.road.master.PetShelter.infrastructure.persistence.pet.PetEntity;
import com.road.master.PetShelter.infrastructure.persistence.user.DoctorEntity;
import com.road.master.PetShelter.infrastructure.persistence.user.IDoctorRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CreateMedicalAppointmentTest extends BaseTestConfiguration{

  @Autowired
  private IPetRepository petRepository;

  @Autowired
  private IDoctorRepository doctorRepository;

  @Before
  public void init() {
    WebTestClient client =
            MockMvcWebTestClient.bindToApplicationContext(context)
                    .configureClient()
                    .baseUrl("/graphql")
                    .build();

    graphQlTester = HttpGraphQlTester.create(client);
  }

  @Test
  @WithMockUser
  public void shouldCreateMedicalAppointment(){
    petRepository.save(new PetEntity("082e5f76-a4c1-4915-a486-935b29d18814", "test", "labrador"));
    doctorRepository.save(new DoctorEntity("abc", "juan", "reyes", "general"));

    //Create a medical appointment
    Map<String, String> createdAppointment =  graphQlTester.documentName("create-medical-appointment")
            .execute()
            .path("data.createMedicalAppointment")
            .entity(Map.class)
            .get();

    //Request created medical appointment
    List<Map> medicalAppointments =  graphQlTester.documentName("get-medical-appointments")
            .execute()
            .path("data.getMedicalAppointments")
            .entityList(Map.class)
            .get();

    assertEquals(medicalAppointments.size(), 1);
  }
}
