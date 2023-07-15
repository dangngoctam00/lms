package com.example.lmsbackend.dto.response;

import lombok.Data;

@Data
public abstract class AbstractPagedDto {
    private Integer page;
    private Integer perPage;
    private Long total;
    private Integer totalPages;
}
