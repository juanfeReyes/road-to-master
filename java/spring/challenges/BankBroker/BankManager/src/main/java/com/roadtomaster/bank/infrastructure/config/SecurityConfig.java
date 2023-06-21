package com.roadtomaster.bank.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain web(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth ->
                    auth
                            .requestMatchers("/swagger-ui/**")
                            .permitAll()
                            .requestMatchers("/v3/api-docs/**")
                            .permitAll()
                            .requestMatchers( "/banks").hasRole("managers")
                            .requestMatchers(HttpMethod.POST, "/users").hasRole("managers")
                            .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("managers", "users")
                            .requestMatchers(HttpMethod.PUT, "/users").hasRole("users")
                            .requestMatchers( "/accounts").hasRole("users")
                            .requestMatchers(HttpMethod.POST, "/transaction").hasRole("users")
                            .requestMatchers(HttpMethod.GET, "/transaction").hasAnyRole("managers", "users")
                            .requestMatchers(HttpMethod.PUT, "/transaction").hasRole("managers")
                            .anyRequest()
                            .denyAll()
            ).oauth2ResourceServer(auth -> auth.jwt().jwtAuthenticationConverter(getJwtAuthenticationConverter()));

    return http.build();
  }

  public Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      Collection<GrantedAuthority> authorities = new java.util.ArrayList<>(Collections.emptyList());
      Collection<String> roleClaim = (List<String>) jwt.getClaimAsMap("realm_access").get("roles");
      authorities.addAll(roleClaim.stream()
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
              .collect(Collectors.toSet()));
      return authorities;
    });
    return converter;
  }
}
