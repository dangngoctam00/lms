package com.example.lmsbackend.domain;

import com.example.lmsbackend.domain.composite_id.UserRoleId;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_has_role")
@Data
public class UserRoleEntity {
    @EmbeddedId
    private UserRoleId id = new UserRoleId();

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;


}
