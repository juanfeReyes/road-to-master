package com.road.master.PetShelter.infrastructure.api;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  @Value("${springdoc.oAuthFlow.authorizationUrl}")
  private String authorizationUrl;

  @Value("${springdoc.oAuthFlow.tokenUrl}")
  private String tokenUrl;

  @Value("${api-version}")
  private String apiVersion;

  @Bean
  public OpenAPI openAPI() {

    return new OpenAPI()
            .info(buildOpenApiInfo())
        .components(
            new Components()
                .addSecuritySchemes("OAuthScheme", new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .description("OAuth2 authentication")
                    .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                            .authorizationUrl(authorizationUrl)
                            .tokenUrl(tokenUrl)
                            .scopes(new Scopes()
                                .addString("full_access", "access pets")
                            )
                        )))
        )
        .addSecurityItem(new SecurityRequirement().addList("OAuthScheme"));
  }

  private Info buildOpenApiInfo(){
    var apiInfo = new Info()
            .title("Pet API")
            .version(apiVersion)
            .description("Pet API open api documentation")
            .license(null);
    return apiInfo;
  }
}
