package com.example.lmsbackend.service;

import com.example.lmsbackend.dto.response.user.PermissionDTO;
import com.example.lmsbackend.mapper.PermissionDTOMapper;
import com.example.lmsbackend.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(PermissionDTOMapper.INSTANCE::mapFromPermissionEntity)
                .collect(Collectors.toList());
    }
}
