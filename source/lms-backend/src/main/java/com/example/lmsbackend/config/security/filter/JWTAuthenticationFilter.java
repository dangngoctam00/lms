package com.example.lmsbackend.config.security.filter;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.jwt.JWTUtils;
import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.enums.StatusCode;
import com.example.lmsbackend.exceptions.jwt.ExpiredJwtException;
import com.example.lmsbackend.exceptions.jwt.JwtInvalidException;
import com.example.lmsbackend.multitenancy.utils.TenantContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().contains("/login") || request.getRequestURI().contains("/register") || request.getRequestURI().contains("/auth/refresh");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        var bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            var token = bearerToken.substring(7);
            try {
                var tenantId = jwtUtils.getTenantId(token);
                TenantContext.setTenantId(tenantId);
                var username = jwtUtils.getUsername(token);
                var userId = jwtUtils.getUserId(token);
                var accountType = jwtUtils.getAccountType(token);

                var currUser = new CustomUserDetails(userId, username, accountType);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(currUser, null, new ArrayList<>());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (ExpiredJwtException ex) {
                response.setStatus(HttpStatus.BAD_GATEWAY.value());
                response.setHeader("Content-Type", "application/json");
                response.getWriter().write(convertObjectToJson(buildErrorResponse(StatusCode.JWT_EXPIRES, ex.getMessage())));
                return;
            } catch (JwtInvalidException ex) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(convertObjectToJson(buildErrorResponse(StatusCode.JWT_INVALID, ex.getMessage())));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private BaseResponse buildErrorResponse(StatusCode errorCode, String errorMessage) {
        BaseResponse errorResponse = new BaseResponse();
        errorResponse.setStatusCode(errorCode);
        errorResponse.setMessage(errorMessage);
        return errorResponse;
    }
}