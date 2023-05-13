package com.roadtomaster.BankBroker.Shared.Persistence;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

public class PostgresDatasourceConfiguration {

  @Bean
  @ConfigurationProperties("spring.datasource.todos")
  public DataSourceProperties postgresDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource postgresDataSource() {
    return postgresDataSourceProperties()
            .initializeDataSourceBuilder()
            .build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
          DataSource dataSource,  EntityManagerFactoryBuilder builder) {
    return builder
            .dataSource(postgresDataSource())
            .build();
  }

  @Bean
  public PlatformTransactionManager postgresTransactionManager(
          LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory) {
    return new JpaTransactionManager(Objects.requireNonNull(postgresEntityManagerFactory.getObject()));
  }
}
