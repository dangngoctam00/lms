package com.example.lmsbackend.multitenancy.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "package")
@Data
public class PackageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private long price;

    @Column(name = "number_of_months")
    private int numberOfMonths;
}
