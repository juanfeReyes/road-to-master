package com.r2m.batch_api.infrastructure.persistence;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfiguration {

    @Autowired
    private DataSource dataSource;

    @PostConstruct()
    private void startMigrations() {
        var config = new ClassicConfiguration();
        config.setDataSource(dataSource);
        config.setLocations(new Location("classpath:migrations/"));

        Flyway flyway = new Flyway(config);
        flyway.migrate();
    }
}
