package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.fill_in_blank.FillInBlankOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FillInBlankOptionRepository extends JpaRepository<FillInBlankOptionEntity, Long> {
}
