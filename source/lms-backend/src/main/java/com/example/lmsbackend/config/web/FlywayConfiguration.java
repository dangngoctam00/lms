package com.example.lmsbackend.config.web;

import com.example.lmsbackend.multitenancy.config.tenant.CurrentTenantIdentifierResolverImpl;
import com.example.lmsbackend.multitenancy.dto.TenantDto;
import com.example.lmsbackend.multitenancy.repository.TenantRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfiguration {

    @Value("${flyway.user}")
    private String username;

    @Value("${flyway.password}")
    private String password;

    @Value("${flyway.url}")
    private String url;

    @Value("${flyway.master.location}")
    private String masterDatabaseFolder;

    @Value("${flyway.tenant.location}")
    private String tenantDatabaseFolder;

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .locations(masterDatabaseFolder)
                .outOfOrder(true)
                .dataSource(dataSource)
                .schemas(CurrentTenantIdentifierResolverImpl.MASTER_SCHEMA)
                .load();
        flyway.migrate();
        return flyway;
    }

    public DataSource getLmsDataSource() {
        var dataSource = new HikariDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl(url);
        return dataSource;
    }

    public MigrateResult migrate(TenantDto dto) {
        return Flyway.configure()
                .dataSource(getLmsDataSource())
                .locations(tenantDatabaseFolder)
                .schemas(dto.getDomain())
                .load()
                .migrate();
    }

    @Bean
    CommandLineRunner commandLineRunner(TenantRepository tenantRepository, DataSource dataSource) {
        return args -> tenantRepository.findAll().forEach(tenant -> {
            String schema = tenant.getSchema();
            Flyway flyway = Flyway.configure()
                    .locations(tenantDatabaseFolder)
                    .dataSource(dataSource)
                    .schemas(schema)
                    .load();
            flyway.migrate();
        });
    }
}