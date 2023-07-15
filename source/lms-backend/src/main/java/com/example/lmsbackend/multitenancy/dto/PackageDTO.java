package com.example.lmsbackend.multitenancy.dto;

import lombok.Data;

@Data
public class PackageDTO {
    private int id;
    private long price;
    private int numberOfMonths;
}
