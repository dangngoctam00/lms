package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.fill_in_blank_dnd_question.FillInBlankDragAndDropAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FillInBlankDragAndDropAnswerRepository extends JpaRepository<FillInBlankDragAndDropAnswerEntity, Long> {
}
