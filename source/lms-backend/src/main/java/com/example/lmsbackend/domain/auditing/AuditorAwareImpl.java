package com.example.lmsbackend.domain.auditing;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UnauthorizedException();
        }
        var username = (String) ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        return Optional.ofNullable(username);
    }
}
