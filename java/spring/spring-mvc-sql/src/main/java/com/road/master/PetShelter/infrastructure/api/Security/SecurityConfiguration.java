package com.road.master.PetShelter.infrastructure.api.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("!test")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .exceptionHandling()
        .and()
        .authorizeRequests()
        .antMatchers("/swagger-ui.html/**", "/swagger-ui/**", "/api-docs/**", "/graphql/**")
        .permitAll()
//        .anyRequest()
//        .authenticated()
        .and()
        .oauth2ResourceServer(oauth2 -> oauth2.jwt().jwtAuthenticationConverter(getJwtAuthToken()));
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
        .ignoring()
        .antMatchers(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs/**",
                "/graphql/**");
  }

  JwtAuthenticationConverter getJwtAuthToken() {
    var converter = new JwtAuthenticationConverter();
    var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
    grantedAuthoritiesConverter.setAuthorityPrefix("");
    converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
    return converter;
  }
}
