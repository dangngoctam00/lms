package com.example.lmsbackend.service;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.domain.PermissionEntity;
import com.example.lmsbackend.domain.RoleEntity;
import com.example.lmsbackend.domain.RolePermissionEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.dto.request.role.CreateRoleRequest;
import com.example.lmsbackend.dto.request.role.PermissionForRole;
import com.example.lmsbackend.dto.request.role.UpdateRoleRequest;
import com.example.lmsbackend.dto.response.role.RoleDTO;
import com.example.lmsbackend.dto.response.role.RolePermissionDTO;
import com.example.lmsbackend.dto.response.user.RoleOfUserDTO;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.enums.RoleEnum;
import com.example.lmsbackend.exceptions.UserNotFoundException;
import com.example.lmsbackend.exceptions.role.DeleteDefaultRoleException;
import com.example.lmsbackend.exceptions.role.RoleNotFoundException;
import com.example.lmsbackend.mapper.PermissionOfRoleDTOMapper;
import com.example.lmsbackend.mapper.RoleDTOMapper;
import com.example.lmsbackend.mapper.RoleOfUserDTOMapperImpl;
import com.example.lmsbackend.mapper.RolePermissionEntityMapper;
import com.example.lmsbackend.repository.RolePermissionRepository;
import com.example.lmsbackend.repository.RoleRepository;
import com.example.lmsbackend.repository.UserRepository;
import com.example.lmsbackend.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    private final RoleOfUserDTOMapperImpl roleOfUserDTOMapper;

    private final EntityManager entityManager;

    @Auth(permission = {PermissionEnum.VIEW_DETAIL_ROLE})
    @Transactional
    @Cacheable("role")
    public RolePermissionDTO getRole(Long resourceId) {
        RoleEntity roleEntity = roleRepository.getById(resourceId);
        List<RolePermissionEntity> rolePermissions = rolePermissionRepository.findAllByRole_Id(resourceId);
        RolePermissionDTO role = new RolePermissionDTO();
        role.setId(resourceId);
        role.setTitle(roleEntity.getTitle());
        role.setDescription(roleEntity.getDescription());
        role.setPermissions(
                rolePermissions.stream()
                        .map(PermissionOfRoleDTOMapper.INSTANCE::mapFromRolePermissionEntity)
                        .collect(Collectors.toList())
        );
        return role;
    }

    @Transactional
    public List<RoleOfUserDTO> getMyRoles() {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isPresent()) {
            UserEntity user = userEntityOptional.get();
            return user.getUserRoleEntities().stream().map(roleOfUserDTOMapper::mapFromUserRoleEntity).collect(Collectors.toList());
        } else {
            throw new UserNotFoundException();
        }
    }

    @Auth(permission = {PermissionEnum.VIEW_LIST_ROLE})
    @Transactional
    @Cacheable("roles")
    public List<RoleDTO> getAllRoles() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        return roleEntities.stream().map(RoleDTOMapper.INSTANCE::mapFromRoleEntity).collect(Collectors.toList());
    }

    @Auth(permission = {PermissionEnum.CREATE_ROLE})
    @Transactional
    public Long createRole(CreateRoleRequest request) {
        log.info("RoleService.createRole request: {}", request);
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setTitle(request.getTitle());
        roleEntity.setDescription(request.getDescription());

        roleEntity = entityManager.merge(roleEntity);
        roleRepository.save(roleEntity);

        for (PermissionForRole permission : request.getPermissions()) {
            RolePermissionEntity entity = RolePermissionEntityMapper.INSTANCE.mapFromPermissionForRole(permission);
            entity.getId().setPermissionId(permission.getId());
            entity.getId().setRoleId(roleEntity.getId());
            entity.setRole(roleEntity);
            entity.setPermission(entityManager.getReference(PermissionEntity.class, permission.getId()));
            rolePermissionRepository.save(entityManager.merge(entity));
        }

        return roleEntity.getId();
    }

    @Auth(permission = {PermissionEnum.UPDATE_ROLE})
    @Transactional
    public Long updateRole(UpdateRoleRequest request, Long resourceId) {
        log.info("RoleService.updateRole request: {}", request);
        RoleEntity roleEntity = roleRepository.getById(request.getRoleId());
        roleEntity.setTitle(request.getTitle());
        roleEntity.setDescription(request.getDescription());

        roleRepository.save(roleEntity);

        rolePermissionRepository.deleteAllById_RoleId(request.getRoleId());

        for (PermissionForRole permission : request.getPermissions()) {
            RolePermissionEntity entity = RolePermissionEntityMapper.INSTANCE.mapFromPermissionForRole(permission);
            entity.setRole(roleEntity);
            entity.getId().setPermissionId(permission.getId());
            entity.getId().setRoleId(request.getRoleId());
            entity.setPermission(entityManager.getReference(PermissionEntity.class, permission.getId()));
            rolePermissionRepository.save(entityManager.merge(entity));
        }


        return roleEntity.getId();
    }


    @Auth(permission = {PermissionEnum.DELETE_ROLE})
    @Transactional
    public Long deleteRole(Long resourceId) {
        if (!roleRepository.existsById(resourceId)) {
            throw new RoleNotFoundException();
        }
        if (RoleEnum.getDefaultRoleIds().contains(resourceId)) {
            throw new DeleteDefaultRoleException();
        }
        userRoleRepository.deleteAllById_RoleId(resourceId);
        roleRepository.deleteById(resourceId);
        return resourceId;
    }
}
