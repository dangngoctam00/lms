package com.example.lmsbackend.multitenancy.config.tenant;

import com.example.lmsbackend.multitenancy.exception.TenantNotFoundException;
import com.example.lmsbackend.multitenancy.repository.TenantRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SchemaBasedMultiTenantConnectionProvider implements MultiTenantConnectionProvider {

    private final String masterSchema;
    private final transient DataSource datasource;
    private final transient TenantRepository tenantRepository;
    private final Long maximumSize;
    private final Integer expireAfterAccess;

    private transient LoadingCache<String, String> tenantSchemas;

    @PostConstruct
    private void createCache() {
        tenantSchemas = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireAfterAccess, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @NonNull
                    public String load(@NonNull String tenantId) {
                        if (CurrentTenantIdentifierResolverImpl.MASTER_SCHEMA.equals(tenantId)) {
                            return tenantId;
                        }
                        log.debug("Trying get tenant schema from tenant id: {}", tenantId);
                        var tenant = tenantRepository.findById(tenantId)
                                .orElseThrow(() -> new TenantNotFoundException(tenantId));
                        return tenant.getSchema();
                    }
                });
    }

    @Autowired
    public SchemaBasedMultiTenantConnectionProvider(
            @Value("${multitenancy.master.schema:#{null}}")
                    String masterSchema,
            DataSource datasource,
            TenantRepository tenantRepository,
            @Value("${multitenancy.schema-cache.maximumSize:1000}")
                    Long maximumSize,
            @Value("${multitenancy.schema-cache.expireAfterAccess:10}")
                    Integer expireAfterAccess) {
        this.masterSchema = masterSchema;
        this.datasource = datasource;
        this.tenantRepository = tenantRepository;
        this.maximumSize = maximumSize;
        this.expireAfterAccess = expireAfterAccess;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return datasource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        log.debug("Get connection for tenant {}", tenantIdentifier);
        String tenantSchema;
        try {
            tenantSchema = tenantSchemas.get(tenantIdentifier);
        } catch (ExecutionException e) {
            throw new TenantNotFoundException(tenantIdentifier);
        }
        final Connection connection = getAnyConnection();
        connection.setSchema(tenantSchema);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        log.debug("Release connection for tenant {}", tenantIdentifier);
        connection.setSchema(masterSchema);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return MultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if ( MultiTenantConnectionProvider.class.isAssignableFrom(unwrapType) ) {
            return (T) this;
        } else {
            throw new UnknownUnwrapTypeException( unwrapType );
        }
    }
}
