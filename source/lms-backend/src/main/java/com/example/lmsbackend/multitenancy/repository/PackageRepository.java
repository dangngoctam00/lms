package com.example.lmsbackend.multitenancy.repository;

import com.example.lmsbackend.multitenancy.domain.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Integer> {

}
