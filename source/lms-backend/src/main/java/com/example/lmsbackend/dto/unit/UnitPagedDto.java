package com.example.lmsbackend.dto.unit;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UnitPagedDto extends AbstractPagedDto {

    private List<UnitItemDto> listData;
}
