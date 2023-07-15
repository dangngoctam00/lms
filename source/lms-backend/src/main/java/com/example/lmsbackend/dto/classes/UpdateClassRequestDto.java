package com.example.lmsbackend.dto.classes;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class UpdateClassRequestDto {
    @NotNull(message = "This is required field")
    private String name;

    private String avatar;

    private LocalDate startedAt;

    private LocalDate endedAt;

    @Pattern(regexp = "^(CREATED|ONGOING|ENDED)$",
            message = "For the Status when updating class, only values CREATED, ONGOING, ENDED are accepted")
    private String status;

    @NotNull(message = "This is required field")
    @Pattern(regexp = "^(ONLINE|OFFLINE|HYBRID)$",
            message = "For the Type, only values ONLINE, OFFLINE, HYBRID are accepted")
    private String type;

    private Boolean isActive;
}
