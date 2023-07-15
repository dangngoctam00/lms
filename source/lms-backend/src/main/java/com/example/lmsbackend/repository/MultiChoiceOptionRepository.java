package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.multi_choice.MultiChoiceOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiChoiceOptionRepository extends JpaRepository<MultiChoiceOptionEntity, Long> {
}
