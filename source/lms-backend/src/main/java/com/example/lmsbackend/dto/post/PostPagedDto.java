package com.example.lmsbackend.dto.post;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Data;

import java.util.List;

@Data
public class PostPagedDto extends AbstractPagedDto {
    private List<PostDto> listData;
}
