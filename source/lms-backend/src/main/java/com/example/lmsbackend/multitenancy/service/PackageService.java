package com.example.lmsbackend.multitenancy.service;

import com.example.lmsbackend.multitenancy.dto.PackageDTO;
import com.example.lmsbackend.multitenancy.mapper.PackageMapper;
import com.example.lmsbackend.multitenancy.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageService {
    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;
    public List<PackageDTO> getAllPackages() {
        return packageRepository.findAll()
                .stream().map(packageMapper::mapFromPackageEntity)
                .collect(Collectors.toList());
    }
}
