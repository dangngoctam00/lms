package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.TenantConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantConfigRepository extends JpaRepository<TenantConfigEntity, String> {
}
