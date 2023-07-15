package com.example.lmsbackend.dto.classes;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
public class CreateClassRequestDto {

    @NotNull(message = "This is required field")
    private String name;

    @NotNull(message = "This is required field")
    private String code;

    private String avatar;

    private LocalDate startedAt;

    private LocalDate endedAt;

    @Pattern(regexp = "^(CREATED|ONGOING)$",
            message = "For the Status when creating class, only values CREATED, ONGOING are accepted")
    private String status;

    @NotNull(message = "This is required field")
    @Pattern(regexp = "^(ONLINE|OFFLINE|HYBRID)$",
            message = "For the Type, only values ONLINE, OFFLINE, HYBRID are accepted")
    private String type;

    @NotNull(message = "This is required field")
    private Long courseId;
}
