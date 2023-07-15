package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.UserPermissionEntity;
import com.example.lmsbackend.domain.composite_id.UserPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermissionEntity, UserPermissionId> {
    Optional<UserPermissionEntity> findById_UserIdAndId_PermissionId(Long userId, int permissionId);

    @Query("SELECT e FROM UserPermissionEntity e WHERE e.id.userId = :userId")
    List<UserPermissionEntity> findAllByUserIdAndAvailable(@Param("userId") Long userId);

    void deleteAllById_UserId(Long userId);
}
