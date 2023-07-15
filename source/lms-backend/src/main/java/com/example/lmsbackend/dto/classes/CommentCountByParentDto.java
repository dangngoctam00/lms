package com.example.lmsbackend.dto.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCountByParentDto {
    private Long parentId;
    private Long count;
}
