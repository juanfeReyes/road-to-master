package com.r2m.batch_api.infrastructure.batch;

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration extends DefaultBatchConfiguration {

    @Bean
    public DataSource myCustomBatchDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/r2m_db?currentSchema=batch_sc");
        dataSource.setUsername("postgres");
        dataSource.setPassword("password");
        return dataSource;
    }

    @Override
    protected DataSource getDataSource() {
        return myCustomBatchDataSource(); // Use the custom DataSource
    }
}
