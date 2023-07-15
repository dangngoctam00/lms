package com.example.lmsbackend.multitenancy.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tenant")
public class TenantEntity {
    @Id
    @Column(name = "tenant_id")
    private String tenantId;

    private String schema;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String domain;
    private LocalDateTime expireTime;
}
