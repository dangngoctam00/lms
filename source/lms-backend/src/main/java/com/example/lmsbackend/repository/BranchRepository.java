package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<BranchEntity, Long> {
}
