package r2m.spring.db.spring_jdbc.infrastructure.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.cglib.core.DefaultNamingPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.*;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.PostgresDialect;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import r2m.spring.db.spring_jdbc.infrastructure.persistence.converters.StringToTravelMediaType;
import r2m.spring.db.spring_jdbc.infrastructure.persistence.converters.TravelMediaTypeToStringConverter;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableJdbcRepositories
public class JdbcConfig {

    @Bean
    public DataSource dataSource() {
        // Hikari is a DbPool library that handles the connections
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/shipment_db");
        config.setUsername("postgres");
        config.setPassword("password");
        config.setDriverClassName("org.postgresql.Driver");
        config.setPoolName("r2m.hikari.pool");
        config.setMaximumPoolSize(5);

        return new HikariDataSource(config);
    }

    @Bean
    public NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(
           new TravelMediaTypeToStringConverter(),
           new StringToTravelMediaType()
        ));
    }

}
