package com.example.lmsbackend.dto.request.breadcrumb;

import lombok.Data;

@Data
public class GetBreadcrumbTitleRequest {
    private long id;
    private String type;
    private String scope;
}
