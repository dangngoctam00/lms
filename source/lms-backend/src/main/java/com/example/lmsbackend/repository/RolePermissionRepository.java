package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.RolePermissionEntity;
import com.example.lmsbackend.domain.composite_id.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, RolePermissionId> {
    List<RolePermissionEntity> findAllByRole_Id(Long roleId);

    @Query("SELECT enity " +
            "FROM RolePermissionEntity enity " +
            "where enity.id.permissionId = :permissionId " +
            "and enity.id.roleId in (" +
            "   SELECT userRoleEntity.id.roleId " +
            "   FROM UserRoleEntity userRoleEntity " +
            "   where userRoleEntity.id.userId = :userId" +
            ")"
    )
    List<RolePermissionEntity> findAllByUserIdAndPermissionId(@Param("userId") long userId, @Param("permissionId") int permissionId);
    void deleteAllById_RoleId(long roleId);
}
