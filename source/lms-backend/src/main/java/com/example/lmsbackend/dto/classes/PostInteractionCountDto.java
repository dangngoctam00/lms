package com.example.lmsbackend.dto.classes;

import com.example.lmsbackend.enums.InteractionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInteractionCountDto {
    private InteractionType type;
    private Long count;
}
