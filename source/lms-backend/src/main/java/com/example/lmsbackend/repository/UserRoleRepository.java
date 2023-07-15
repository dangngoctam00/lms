package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.UserRoleEntity;
import com.example.lmsbackend.domain.composite_id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleId> {
    @Query("SELECT entity FROM UserRoleEntity entity where entity.id.userId = :userId and entity.validFrom < current_timestamp and entity.expiresAt > current_timestamp")
    List<UserRoleEntity> findAllByUserIdAndAvailable(@Param("userId") Long userId);

    @Query("SELECT entity FROM UserRoleEntity entity where entity.id.userId = :userId")
    List<UserRoleEntity> findAllByUserId(@Param("userId") Long userId);

    void deleteAllById_UserId(Long userId);
    void deleteAllById_RoleId(Long roleId);
}
