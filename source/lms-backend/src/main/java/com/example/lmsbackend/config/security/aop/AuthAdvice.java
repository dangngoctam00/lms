package com.example.lmsbackend.config.security.aop;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.caching.PermissionCaching;
import com.example.lmsbackend.domain.RolePermissionEntity;
import com.example.lmsbackend.domain.UserPermissionEntity;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.exceptions.UnauthorizedException;
import com.example.lmsbackend.mapper.PermissionSecurityMapper;
import com.example.lmsbackend.repository.RolePermissionRepository;
import com.example.lmsbackend.repository.UserPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthAdvice {
    public static final String RESOURCE_ID_PARAMETER_NAME = "resourceId";
    private final RuleFactory ruleFactory;
    private final UserPermissionRepository userPermissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionCaching permissionCaching;

    @Pointcut("@annotation(Auth)")
    public void hasAuthAnnotation() {
        // Do nothing
    }

    @Before(value = "hasAuthAnnotation()")
    public void authorize(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        List<String> parameterNames = Arrays.asList(signature.getParameterNames());

        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currUser.getId() == 1) return;

        if (parameterNames.contains(RESOURCE_ID_PARAMETER_NAME)) {
            int index = parameterNames.indexOf(RESOURCE_ID_PARAMETER_NAME);
            if (Number.class.isAssignableFrom(joinPoint.getArgs()[index].getClass())) {
                Long resourceId = ((Number) joinPoint.getArgs()[index]).longValue();
                authorizeWithResourceId(joinPoint, resourceId);
            } else {
                String resourceId = (String) joinPoint.getArgs()[index];
                authorizeWithResourceId(joinPoint, resourceId);
            }
        } else {
            authorizeWithoutResourceId(joinPoint);
        }
    }

    private void authorizeWithResourceId(JoinPoint joinPoint, Long id) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Auth authAnnotation = method.getAnnotation(Auth.class);

        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // tự tác động đến bản thân
        if (authAnnotation.selfCheck() && currUser.getId().equals(id)) return;

        for (PermissionEnum permissionEnum : authAnnotation.permission()) {
            List<PermissionSecurity> permissions = findAllPermission(currUser.getId(), permissionEnum.getId());
            permissionCaching.addPermission(currUser.getId(), permissionEnum, permissions);
            for (PermissionSecurity permission : permissions) {
                boolean isPermitted = ruleFactory.getRule(permissionEnum.getResourceType())
                        .authorize(currUser, permission, id);
                if (isPermitted) return;
            }
        }
        throw new UnauthorizedException("User doesn't have authorization to access resource");
    }

    private void authorizeWithResourceId(JoinPoint joinPoint, String id) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Auth authAnnotation = method.getAnnotation(Auth.class);

        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // tự tác động đến bản thân
        if (authAnnotation.selfCheck() && currUser.getId().equals(id)) return;

        for (PermissionEnum permissionEnum : authAnnotation.permission()) {
            List<PermissionSecurity> permissions = findAllPermission(currUser.getId(), permissionEnum.getId());
            permissionCaching.addPermission(currUser.getId(), permissionEnum, permissions);
            for (PermissionSecurity permission : permissions) {
                boolean isPermitted = ruleFactory.getRule(permissionEnum.getResourceType())
                        .authorize(currUser, permission, id);
                if (isPermitted) return;
            }
        }
        throw new UnauthorizedException("User doesn't have authorization to access resource");
    }

    private List<PermissionSecurity> findAllPermission(Long userId, int permissionId) {
        List<PermissionSecurity> permissions = new ArrayList<>();
        Optional<UserPermissionEntity> userPermissionEntityOptional = userPermissionRepository.findById_UserIdAndId_PermissionId(userId, permissionId);
        userPermissionEntityOptional.ifPresent(userPermissionEntity -> permissions.add(PermissionSecurityMapper.INSTANCE.mapFromUserPermissionEntity(userPermissionEntity)));
        List<RolePermissionEntity> rolePermissionEntities = rolePermissionRepository.findAllByUserIdAndPermissionId(userId, permissionId);
        permissions.addAll(rolePermissionEntities.stream().map(PermissionSecurityMapper.INSTANCE::mapFromRolePermissionEntity).collect(Collectors.toList()));
        return permissions;
    }

    private void authorizeWithoutResourceId(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Auth authAnnotation = method.getAnnotation(Auth.class);

        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        for (PermissionEnum permission : authAnnotation.permission()) {
            if (checkUserHasPermission(currUser.getId(), permission)) {
                return;
            }
        }
        throw new UnauthorizedException("User doesn't have authorization to access resource");
    }

    private boolean checkUserHasPermission(Long userId, PermissionEnum permissionEnum) {
        Optional<UserPermissionEntity> userPermissionEntityOptional = userPermissionRepository.findById_UserIdAndId_PermissionId(userId, permissionEnum.getId());
        if (userPermissionEntityOptional.isPresent()) return true;
        List<RolePermissionEntity> rolePermissionEntities = rolePermissionRepository.findAllByUserIdAndPermissionId(userId, permissionEnum.getId());
        var permissions = rolePermissionEntities.stream().map(PermissionSecurityMapper.INSTANCE::mapFromRolePermissionEntity).collect(Collectors.toList());
        permissionCaching.addPermission(userId, permissionEnum, permissions);
        return !rolePermissionEntities.isEmpty();
    }

}
