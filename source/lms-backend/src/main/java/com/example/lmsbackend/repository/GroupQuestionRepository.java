package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.base_question.GroupQuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionInGroupEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupQuestionRepository extends JpaRepository<GroupQuestionEntity, Long> {

    @EntityGraph("group-question")
    Optional<GroupQuestionEntity> findById(Long id);

    @Query("SELECT q FROM QuestionInGroupEntity q LEFT JOIN FETCH q.group WHERE q.question.id = ?1")
    Optional<QuestionInGroupEntity> findGroupByQuestion(Long questionId);

    @Query("SELECT u.question.id FROM QuestionInGroupEntity u WHERE u.group.id = ?1")
    List<Long> findQuestionInGroup(Long groupId);

    @Query("SELECT q FROM GroupQuestionEntity q " +
            "LEFT JOIN q.questions p " +
            "LEFT JOIN FETCH q.question " +
            "WHERE p.question.id = ?1")
    Optional<GroupQuestionEntity> findQuestionGroupByQuestionInGroup(Long questionId);

    @Query("SELECT q.group FROM QuestionInGroupEntity q WHERE q.question.id = ?1")
    Optional<GroupQuestionEntity> findGroupQuestionByQuestionInGroup(Long id);
}