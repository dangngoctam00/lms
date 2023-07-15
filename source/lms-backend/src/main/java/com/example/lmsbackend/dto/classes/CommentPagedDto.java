package com.example.lmsbackend.dto.classes;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Data;

import java.util.List;

@Data
public class CommentPagedDto extends AbstractPagedDto {
    private List<CommentDto> listData;
}
