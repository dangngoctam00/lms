package com.example.lmsbackend.multitenancy.dto;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class GetPackagesResponse extends BaseResponse {
    private List<PackageDTO> packages;
}
