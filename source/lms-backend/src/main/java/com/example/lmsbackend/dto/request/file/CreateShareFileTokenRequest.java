package com.example.lmsbackend.dto.request.file;

import lombok.Data;

import java.util.List;

@Data
public class CreateShareFileTokenRequest {
    private List<String> fullpaths;
}
