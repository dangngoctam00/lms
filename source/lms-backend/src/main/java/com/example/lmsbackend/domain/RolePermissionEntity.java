package com.example.lmsbackend.domain;

import com.example.lmsbackend.domain.composite_id.RolePermissionId;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "role_has_permission")
public class RolePermissionEntity {

    @EmbeddedId
    private RolePermissionId id = new RolePermissionId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionId")
    private PermissionEntity permission;

    @Column(name = "is_limit_by_branch")
    private Integer isLimitByBranch;

    @Column(name = "is_limit_by_teaching")
    private Integer isLimitByTeaching;

    @Column(name = "is_limit_by_dean")
    private Integer isLimitByDean;

    @Column(name = "is_limit_by_manager")
    private Integer isLimitByManager;

    @Column(name = "is_limit_by_learn")
    private Integer isLimitByLearn;

    @Column(name = "is_limit_by_owner")
    private Integer isLimitByOwner;
}
