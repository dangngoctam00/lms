package com.example.lmsbackend.multitenancy.config.tenant;

import com.example.lmsbackend.multitenancy.utils.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component("currentTenantIdentifierResolver")
@Slf4j
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    public static final String MASTER_SCHEMA = "public";
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getTenantId();
        if (!StringUtils.isEmpty(tenantId)) {
//            log.debug("Resolve current tenant id: {}", tenantId);
            return tenantId;
        } else {
//            log.debug("Cannot resolve current tenant, fallback to master tenant");
            return MASTER_SCHEMA;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
