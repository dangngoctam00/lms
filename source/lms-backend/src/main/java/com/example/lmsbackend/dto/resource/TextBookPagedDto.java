package com.example.lmsbackend.dto.resource;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TextBookPagedDto extends AbstractPagedDto {
    List<TextbookDto> listData;
}
