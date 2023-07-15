package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.fill_in_blank_with_choices.FillInBlankMultiChoiceBlankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FillInBlankMultiChoiceBlankRepository extends JpaRepository<FillInBlankMultiChoiceBlankEntity, Long> {
}
