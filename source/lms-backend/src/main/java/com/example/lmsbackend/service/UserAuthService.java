package com.example.lmsbackend.service;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.domain.RolePermissionEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.UserPermissionEntity;
import com.example.lmsbackend.domain.UserRoleEntity;
import com.example.lmsbackend.dto.response.user.LimitPermissionOfUserDTO;
import com.example.lmsbackend.dto.response.user.PermissionOfUserDTO;
import com.example.lmsbackend.dto.response.user.RoleOfUserDTO;
import com.example.lmsbackend.exceptions.UserNotFoundException;
import com.example.lmsbackend.mapper.LimitPermissionOfUserDTOMapper;
import com.example.lmsbackend.mapper.PermissionOfUserDTOMapper;
import com.example.lmsbackend.mapper.RoleOfUserDTOMapperImpl;
import com.example.lmsbackend.repository.RolePermissionRepository;
import com.example.lmsbackend.repository.UserPermissionRepository;
import com.example.lmsbackend.repository.UserRepository;
import com.example.lmsbackend.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleOfUserDTOMapperImpl roleOfUserDTOMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isPresent()) {
            return new CustomUserDetails(userEntity.get().getId(), username, userEntity.get().getAccountType());
        } else {
            throw new UsernameNotFoundException("Cannot find user");
        }
    }

    @Transactional
    public List<RoleOfUserDTO> getRoles(Long userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isPresent()) {
            UserEntity user = userEntityOptional.get();
            return user.getUserRoleEntities().stream().map(roleOfUserDTOMapper::mapFromUserRoleEntity).collect(Collectors.toList());
        } else {
            throw new UserNotFoundException();
        }
    }

    @Transactional
    public List<PermissionOfUserDTO> getPermissions(Long userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isPresent()) {
            UserEntity user = userEntityOptional.get();
            return user.getUserPermissionEntities().stream().map(PermissionOfUserDTOMapper.INSTANCE::mapFromUserPermissionEntity).collect(Collectors.toList());
        } else {
            throw new UserNotFoundException();
        }
    }

    @Transactional
    public List<LimitPermissionOfUserDTO> getLimitPermissionsOfUser() {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new UserNotFoundException();
        }

        Map<Integer, LimitPermissionOfUserDTO> permissionsMap = new HashMap<>();

        List<UserPermissionEntity> userPermissionEntities = userPermissionRepository.findAllByUserIdAndAvailable(userId);
        userPermissionEntities.forEach(userPermissionEntity -> permissionsMap.put(userPermissionEntity.getId().getPermissionId(), LimitPermissionOfUserDTOMapper.INSTANCE.mapFromPermissionEntity(userPermissionEntity)));

        List<UserRoleEntity> userRoleEntities = userRoleRepository.findAllByUserIdAndAvailable(userId);
        userRoleEntities.forEach(userRoleEntity -> {
            List<RolePermissionEntity> rolePermissionEntities = rolePermissionRepository.findAllByRole_Id(userRoleEntity.getId().getRoleId());
            rolePermissionEntities.forEach(rolePermissionEntity -> {
                Integer permissionId = rolePermissionEntity.getId().getPermissionId();
                if (permissionsMap.containsKey(permissionId)) {
                    try {
                        permissionsMap.get(permissionId).merge(userRoleEntity, rolePermissionEntity);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        return new ArrayList<>(permissionsMap.values());
    }
}
