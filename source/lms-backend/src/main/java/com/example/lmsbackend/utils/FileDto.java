package com.example.lmsbackend.utils;

import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

@Data
public class FileDto {

    private String name;
    private String extension;
    private ByteArrayResource bytes;

    public FileDto(String name, String extension, ByteArrayResource bytes) {
        this.name = name;
        this.extension = extension;
        this.bytes = bytes;
    }
}
