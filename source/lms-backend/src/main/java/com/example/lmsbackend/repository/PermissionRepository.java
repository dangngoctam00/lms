package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    List<PermissionEntity> findAllByIdNotIn(List<Integer> permissionIds);
}
