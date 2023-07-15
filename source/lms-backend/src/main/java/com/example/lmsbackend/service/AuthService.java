package com.example.lmsbackend.service;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.config.security.aop.RuleFactory;
import com.example.lmsbackend.config.security.jwt.JWTUtils;
import com.example.lmsbackend.domain.*;
import com.example.lmsbackend.domain.event.CalendarType;
import com.example.lmsbackend.dto.request.auth.*;
import com.example.lmsbackend.dto.response.auth.GrantedPermission;
import com.example.lmsbackend.dto.response.auth.GrantedRole;
import com.example.lmsbackend.dto.response.auth.LoginResponse;
import com.example.lmsbackend.dto.response.auth.RefreshTokenResponse;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.exceptions.UserNotFoundException;
import com.example.lmsbackend.exceptions.UsernameExistedException;
import com.example.lmsbackend.exceptions.UsernameOrPasswordIncorrectException;
import com.example.lmsbackend.mapper.*;
import com.example.lmsbackend.multitenancy.utils.TenantContext;
import com.example.lmsbackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    public static final int ADMIN_ID = 1;
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EventService eventService;

    private final UserRoleRepository userRoleRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final EntityManager entityManager;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final LimitPermissionMapper limitPermissionMapper;
    private final RuleFactory ruleFactory;


    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new UsernameOrPasswordIncorrectException();
            }
            LoginResponse loginResponse = new LoginResponse();
            CustomUserDetails userDetails = new CustomUserDetails(user.getId(), request.getUsername(), user.getAccountType());
            String token = jwtUtils.generateToken(userDetails, TenantContext.getTenantId());
            loginResponse.setToken(token);
            loginResponse.setUser(userMapper.mapToUserDto(user));
            loginResponse.getUser().setCalendarId((eventService.getCalendarId(CalendarType.USER, user.getId()).orElseGet(() -> Long.valueOf("0")).toString()));
            loginResponse.setRefreshToken(jwtUtils.generateRefreshToken(userDetails));
            loginResponse.setTenant(TenantContext.getTenantId());
            loginResponse.setPermissions(getPermissionsMap(user.getId()));
            log.info("User {} login successfully.", user.getUsername());
            return loginResponse;
        } else {
            log.info("User {} not found.", request.getUsername());
            throw new UsernameOrPasswordIncorrectException();
        }
    }

    private Map<PermissionEnum, LimitPermission> getPermissionsMap(Long userId) {
        Map<PermissionEnum, LimitPermission> permissionMap = new EnumMap<>(PermissionEnum.class);
        if (userId == ADMIN_ID) {
            List<PermissionEntity> permissionEntities = permissionRepository.findAll();
            for (PermissionEntity permissionEntity: permissionEntities){
                permissionMap.put(PermissionEnum.fromId(permissionEntity.getId()), new LimitPermission(true, true, true, true, true));
            }
            return permissionMap;
        }
        Map<PermissionEnum, LimitPermission> permissionsUserHas = getAllPermissionUserHas(userId);
        List<PermissionEntity> permissionUserNotHas = permissionRepository.findAllByIdNotIn(permissionsUserHas.keySet().stream().map(PermissionEnum::getId).collect(Collectors.toList()));
        for (PermissionEntity permissionEntity: permissionUserNotHas){
            permissionsUserHas.put(PermissionEnum.fromId(permissionEntity.getId()), null);
        }
        return permissionsUserHas;
    }

    private Map<PermissionEnum, LimitPermission> getAllPermissionUserHas(Long userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        Map<PermissionEnum, LimitPermission> permissionMap = new HashMap<>();

        List<UserPermissionEntity> userPermissionEntities = userPermissionRepository.findAllByUserIdAndAvailable(userId);
        for (UserPermissionEntity userPermissionEntity : userPermissionEntities){
            permissionMap.put(PermissionEnum.fromId(userPermissionEntity.getPermission().getId()), limitPermissionMapper.mapFromUserPermission(userPermissionEntity));
        }

        List<UserRoleEntity> userRoleEntities = userRoleRepository.findAllByUserId(userId);
        userRoleEntities.forEach(userRoleEntity -> {
            List<RolePermissionEntity> rolePermissionEntities = rolePermissionRepository.findAllByRole_Id(userRoleEntity.getId().getRoleId());
            rolePermissionEntities.forEach(rolePermissionEntity -> {
                PermissionEnum permissionEnum = PermissionEnum.fromId(rolePermissionEntity.getPermission().getId());
                if (permissionMap.containsKey(permissionEnum)) {
                    permissionMap.put(permissionEnum, limitPermissionMapper.mapFromLimitPermissionAndRolePermission(permissionMap.get(permissionEnum), rolePermissionEntity));
                } else {
                    permissionMap.put(permissionEnum, limitPermissionMapper.mapFromRolePermission(rolePermissionEntity));
                }
            });
        });

        return permissionMap;
    }


    @Transactional
    public void register(RegisterRequest request) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(request.getUsername());
        if (userEntityOptional.isPresent()) {
            log.info("Username {} is used", request.getUsername());
            throw new UsernameExistedException(request.getUsername());
        }

        var user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .avatar(request.getAvatar())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);
        eventService.createCalendar(CalendarType.USER, user.getId());
        log.info("Register user {} successfully", user);
    }

    @Transactional
    @Auth(permission = {PermissionEnum.GRANT_PERMISSION})
    public Long grantPermissions(GrantPermissionsRequest request) {
        log.info("AuthService.grantPermissions request: {}", request);
        userRoleRepository.deleteAllById_UserId(request.getUserId());
        userPermissionRepository.deleteAllById_UserId(request.getUserId());

        for (GrantedRole grantedRole : request.getRoles()) {
            UserRoleEntity userRoleEntity = UserRoleEntityMapper.INSTANCE
                    .mapFromUserIdAnfGrantedRole(request.getUserId(), grantedRole);
            userRoleRepository.save(entityManager.merge(userRoleEntity));
        }

        for (GrantedPermission grantedPermission : request.getPermissions()) {
            UserPermissionEntity userPermissionEntity = UserPermissionEntityMapper.INSTANCE
                    .mapFromUserIdAndGrantedPermission(request.getUserId(), grantedPermission);
            userPermissionEntity.setUser(entityManager.getReference(UserEntity.class, request.getUserId()));
            userPermissionEntity.setPermission(entityManager.getReference(PermissionEntity.class, grantedPermission.getId()));
            userPermissionRepository.save(entityManager.merge(userPermissionEntity));
        }

        return request.getUserId();
    }

    public RefreshTokenResponse refreshToken(RequestTokenRequest request) {
        // * Generate and return new token
        // * Get the user from the token
        String username = jwtUtils.getUsername(request.getRefreshToken());
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            RefreshTokenResponse response = new RefreshTokenResponse();
            CustomUserDetails userDetails = new CustomUserDetails(user.getId(), username, user.getAccountType());
            String newToken = jwtUtils.generateToken(userDetails, TenantContext.getTenantId());
            response.setToken(newToken);
            response.setRefreshToken(jwtUtils.generateRefreshToken(userDetails));
            log.info("User {} refreshed token successfully.", user.getUsername());
            return response;
        } else {
            log.info("User {} not found.", username);
            throw new UserNotFoundException();
        }
        // * Generate new one
        // * Return
    }

    public boolean checkPermission(CheckPermissionRequest request) {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        request.getPermission().setId(request.getPermissionName().getId());
        return ruleFactory.getRule(request.getPermissionName().getResourceType()).authorize(currUser, request.getPermission(), request.getResourceId());
    }
}
