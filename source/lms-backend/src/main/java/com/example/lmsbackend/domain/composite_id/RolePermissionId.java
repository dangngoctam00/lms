package com.example.lmsbackend.domain.composite_id;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;


@Data
@NoArgsConstructor
@Embeddable
public class RolePermissionId implements Serializable {
    private Long roleId;
    private Integer permissionId;
}
