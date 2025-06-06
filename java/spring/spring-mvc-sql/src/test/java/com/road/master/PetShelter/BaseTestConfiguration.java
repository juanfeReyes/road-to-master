package com.road.master.PetShelter;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestSecurityConfiguration.class)
public class BaseTestConfiguration {

//  @Autowired
//  private InputDestination input;
//
//  @Autowired
//  private OutputDestination output;

  @Autowired
  protected WebApplicationContext context;

//  @Autowired
//  protected TestRestTemplate restTemplate;

  protected GraphQlTester graphQlTester;
}
