package com.example.lmsbackend.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "permission")
public class PermissionEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "has_limit_by_branch")
    private boolean hasLimitByBranch;

    @Column(name = "has_limit_by_teaching")
    private boolean hasLimitByTeaching;

    @Column(name = "has_limit_by_dean")
    private boolean hasLimitByDean;

    @Column(name = "has_limit_by_manager")
    private boolean hasLimitByManager;

    @Column(name = "has_limit_by_learn")
    private boolean hasLimitByLearn;

    @Column(name = "has_limit_by_owner")
    private boolean hasLimitByOwner = false;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "permission")
    private Set<RolePermissionEntity> roles;
}
