package com.example.lmsbackend.multitenancy.repository;

import com.example.lmsbackend.multitenancy.domain.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<TenantEntity, String> {
    Optional<TenantEntity> findByUsername(String username);
    Optional<TenantEntity> findByEmail(String email);

    TenantEntity findByTenantId(String tenantId);
}
