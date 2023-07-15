package com.example.lmsbackend.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum RoleEnum {
    ADMIN(1L),
    EMPLOYEE(2L),
    STUDENT(3L),
    TEACHER(4L);

    private Long id;

    RoleEnum(Long id) {
        this.id = id;
    }

    public static List<Long> getDefaultRoleIds() {
        return Arrays.stream(RoleEnum.class.getEnumConstants()).map(RoleEnum::getId).collect(Collectors.toList());
    }
}
