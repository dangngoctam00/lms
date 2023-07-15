package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.fill_in_blank_with_choices.FillInBlankMultiChoiceOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FillInBlankMultiChoiceOptionRepository extends JpaRepository<FillInBlankMultiChoiceOptionEntity, Long> {
}
