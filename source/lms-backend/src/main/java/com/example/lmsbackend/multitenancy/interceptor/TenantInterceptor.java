package com.example.lmsbackend.multitenancy.interceptor;

import com.example.lmsbackend.multitenancy.exception.MissingTenantIdException;
import com.example.lmsbackend.multitenancy.utils.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

@Slf4j
@Component
@PropertySource("classpath:multitenant.properties")
public class TenantInterceptor implements WebRequestInterceptor {
    private final String masterSchema;

    @Autowired
    public TenantInterceptor(
            @Value("${multitenancy.master.schema:#{null}}") String defaultTenant) {
        this.masterSchema = defaultTenant;
    }

    @Override
    public void preHandle(@NonNull WebRequest request) {
        String tenantId;
        if (StringUtils.isEmpty(TenantContext.getTenantId())) {
            if (request.getHeader("X-TENANT-ID") != null) {
                tenantId = request.getHeader("X-TENANT-ID");
            } else if (this.masterSchema != null) {
                tenantId = this.masterSchema;
            } else {
                throw new MissingTenantIdException();
            }
            TenantContext.setTenantId(tenantId);
//            log.info("Set TENANT to {}", tenantId);
        }
    }

    @Override
    public void postHandle(@NonNull WebRequest request, ModelMap model) {
        TenantContext.clear();
    }

    @Override
    public void afterCompletion(@NonNull WebRequest request, Exception ex) {
        // NOOP
    }
}
