package com.example.lmsbackend.websocket.utils;

import com.example.lmsbackend.multitenancy.exception.MissingTenantIdException;
import com.example.lmsbackend.multitenancy.utils.TenantContext;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

public class RequestUtils {
    public static void extractTenantFromHeaderAndSetToContext(StompHeaderAccessor accessor) {
        var headers = accessor.getNativeHeader("X-TENANT-ID");
        if (isEmpty(headers)) {
            throw new MissingTenantIdException();
        }
        TenantContext.setTenantId(headers.get(0));
    }
}
