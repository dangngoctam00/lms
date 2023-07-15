package com.example.lmsbackend.multitenancy.repository;

import com.example.lmsbackend.multitenancy.domain.TenantCustomizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantCustomizeRepository extends JpaRepository<TenantCustomizeEntity, String> {
}
