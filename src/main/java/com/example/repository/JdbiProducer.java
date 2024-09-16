package com.example.repository;

import org.jdbi.v3.core.Jdbi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import io.agroal.api.AgroalDataSource;
import jakarta.ws.rs.ApplicationPath;

public class JdbiProducer {

    @Inject
    AgroalDataSource dataSource; // O Quarkus usa Agroal para gerenciar DataSources

    @Produces
    @ApplicationScoped
    public Jdbi createJdbi() {
        return Jdbi.create(dataSource);
    }
}
