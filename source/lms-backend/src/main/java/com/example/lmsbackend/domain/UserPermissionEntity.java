package com.example.lmsbackend.domain;

import com.example.lmsbackend.domain.composite_id.UserPermissionId;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_has_permission")
@Data
public class UserPermissionEntity {

    @EmbeddedId
    private UserPermissionId id = new UserPermissionId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private UserEntity user;

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

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

}
