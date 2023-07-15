package com.example.lmsbackend.dto.exam;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamContentPagedDto extends AbstractPagedDto {
    private List<ExamContentItemDto> listData;
}
