package com.road.master.PetShelter;

import org.junit.jupiter.api.Order;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@TestConfiguration
@Profile("test")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TestSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    // register test user with in memory authentication provider
    auth.inMemoryAuthentication().withUser("test").password("pass").roles("ROLES");
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // enable basic authentication & disable anoymous access
    http.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().anonymous().disable();
  }
}
