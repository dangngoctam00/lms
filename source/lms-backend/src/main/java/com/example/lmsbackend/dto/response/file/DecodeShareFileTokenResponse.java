package com.example.lmsbackend.dto.response.file;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class DecodeShareFileTokenResponse extends BaseResponse {
    private List<String> fullpaths;
}
