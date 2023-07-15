package com.example.lmsbackend.websocket.interceptor;

import com.example.lmsbackend.multitenancy.utils.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;

@Configuration
@Slf4j
public class WebSocketInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (CONNECT.equals(accessor != null ? accessor.getCommand() : null)) {
            // ! Need to implement checking jwt after merge PR of LMS-permission
            // ! Currently, only get tenantId from header to set into context.
//            List<String> authorization = accessor.getNativeHeader("Authorization");
//            log.debug("X-Authorization: {}", authorization);
//
//            String accessToken = authorization.get(0).split(" ")[1];
//            Jwt jwt = jwtDecoder.decode(accessToken);
//            JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//            Authentication authentication = converter.convert(jwt);
//            accessor.setUser(authentication);
            var tenantId = accessor.getNativeHeader("X-TENANT-ID");
            if (tenantId != null && !tenantId.isEmpty()) {
                TenantContext.setTenantId(tenantId.get(0));
            }
            else {
                // TODO: throw exception
            }

        }
        return message;
    }
}
