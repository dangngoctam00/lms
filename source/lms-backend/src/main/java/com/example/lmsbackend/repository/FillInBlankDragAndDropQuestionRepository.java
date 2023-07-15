package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.fill_in_blank_dnd_question.FillInBlankDragAndDropQuestionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FillInBlankDragAndDropQuestionRepository extends JpaRepository<FillInBlankDragAndDropQuestionEntity, Long> {

    @EntityGraph("fill-in-blank-drag-and-drop-answers-blanks")
    Optional<FillInBlankDragAndDropQuestionEntity> findById(Long id);
}