package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.domain.exam.ExamEntity;
import com.example.lmsbackend.domain.exam.ExamQuestionSourceEntity;
import com.example.lmsbackend.domain.exam.QExamQuestionSourceEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionType;
import com.example.lmsbackend.dto.exam.QuestionInExamByTypeAndIdDto;
import com.example.lmsbackend.enums.QuestionAnswerStatus;
import com.example.lmsbackend.repository.AnswerQuestionTemporaryRepository;
import com.example.lmsbackend.repository.GroupQuestionRepository;
import com.example.lmsbackend.repository.QuizSessionFlagRepository;
import com.example.lmsbackend.repository.custom.ExamQuestionSourceRepositoryCustom;
import com.example.lmsbackend.repository.custom.PagedList;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ExamQuestionSourceRepositoryCustomImpl implements ExamQuestionSourceRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    private final AnswerQuestionTemporaryRepository answerQuestionTemporaryRepository;

    private final QuizSessionFlagRepository quizSessionFlagRepository;

    private final CriteriaBuilderFactory criteriaBuilderFactory;
    private final GroupQuestionRepository groupQuestionRepository;

    @Override
    public PagedList<QuestionInExamByTypeAndIdDto> getQuestionsIdByExam(Long examId, Integer page, Integer size) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(ExamQuestionSourceEntity.class);
        var root = query.from(ExamQuestionSourceEntity.class);
        var select = query.select(root);
        query.where(cb.equal(root.get(ExamQuestionSourceEntity.Fields.exam).get(ExamEntity.Fields.id), examId));
        query.orderBy(cb.asc(root.get(ExamQuestionSourceEntity.Fields.order)), cb.asc(root.get(ExamQuestionSourceEntity.Fields.id)));
        var typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        typedQuery.setHint(FETCH_GRAPH, entityManager.getEntityGraph("exam-question-source-question"));

        var result = typedQuery.getResultList()
                .stream()
                .map(r -> new QuestionInExamByTypeAndIdDto(r.getQuestion().getId(), r.getQuestion().getQuestion().getType(), r.getOrder()))
                .collect(toList());

        // for count query
        var countQuery = cb.createQuery(Long.class);
        var rootCountQuery = countQuery.from(ExamQuestionSourceEntity.class);
        countQuery.select(cb.count(rootCountQuery));
        entityManager.createQuery(countQuery);
        countQuery.where(cb.equal(rootCountQuery.get(ExamQuestionSourceEntity.Fields.exam).get(ExamEntity.Fields.id), examId));

        var count = entityManager.createQuery(countQuery)
                .getSingleResult();

        return new PagedList<>(result, count, (page - 1) * size, size);
    }

    @Override
    public List<QuestionInExamByTypeAndIdDto> getQuestionsIdAndTypeByExam(UUID sessionId, Long examId) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(ExamQuestionSourceEntity.class);
        var root = query.from(ExamQuestionSourceEntity.class);

        var select = query.select(root);
        query.where(cb.equal(root.get(ExamQuestionSourceEntity.Fields.exam).get(ExamEntity.Fields.id), examId));
        query.orderBy(cb.asc(root.get(ExamQuestionSourceEntity.Fields.order)), cb.asc(root.get(ExamQuestionSourceEntity.Fields.id)));
        var typedQuery = entityManager.createQuery(select);
        typedQuery.setHint(FETCH_GRAPH, entityManager.getEntityGraph("exam-question-source-question"));

        var examQuestionSource = typedQuery.getResultList();
        var results = examQuestionSource
                .stream()
                .map(r -> new QuestionInExamByTypeAndIdDto(r.getQuestion().getId(), r.getQuestion().getQuestion().getType(), r.getOrder()))
                .collect(toList());

        var questionsId = results.stream()
                .map(QuestionInExamByTypeAndIdDto::getId)
                .collect(toList());

        var answeredQuestionsOfSessions = answerQuestionTemporaryRepository.findAnsweredQuestionsOfSessions(sessionId);
        results.forEach(question -> {
            if (StringUtils.equals(question.getType(), QuestionType.GROUP.getType())) {
                var questionsInGroup = groupQuestionRepository.findQuestionInGroup(question.getId());
                var answeredQuestionCount = answeredQuestionsOfSessions.stream()
                        .filter(questionsInGroup::contains)
                        .count();
                if (answeredQuestionCount == 0) {
                    question.setStatus(QuestionAnswerStatus.NONE);
                } else if (answeredQuestionCount < questionsInGroup.size()) {
                    question.setStatus(QuestionAnswerStatus.PARTIALLY_ANSWERED);
                } else {
                    question.setStatus(QuestionAnswerStatus.ANSWERED);
                }
            } else {
                question.setStatus(answeredQuestionsOfSessions.stream()
                        .anyMatch(ans -> Objects.equals(ans, question.getId())) ? QuestionAnswerStatus.ANSWERED : QuestionAnswerStatus.NONE);
            }
        });

        var flaggedQuestionsOfSessions = quizSessionFlagRepository.getFlaggedQuestion(sessionId, questionsId);
        results.forEach(question -> question.setFlagged(flaggedQuestionsOfSessions.stream()
                .anyMatch(flag -> Objects.equals(flag, question.getId()))));
        return results;
    }

    @Override
    public Optional<ExamQuestionSourceEntity> findByExamAndQuestion(Long examId, Long questionId) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(ExamQuestionSourceEntity.class);
        var root = query.from(ExamQuestionSourceEntity.class);
        query.select(root);
        query.where(cb.and(cb.equal(root.get(ExamQuestionSourceEntity.Fields.exam).get(ExamEntity.Fields.id), examId),
                cb.and(cb.equal(root.get(ExamQuestionSourceEntity.Fields.question).get(QuestionEntity.Fields.id), questionId))));
        var result = entityManager.createQuery(query).getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public int getPageOfQuestion(Long examId, Long questionId, int pageSize) {
        var entity = QExamQuestionSourceEntity.examQuestionSourceEntity;
        var query = new BlazeJPAQuery<Long>(entityManager, criteriaBuilderFactory)
                .from(entity)
                .select(entity);
        var indexQuery = new BlazeJPAQuery<Integer>(entityManager, criteriaBuilderFactory)
                .from(entity)
                .select(entity.order);
        var countPreviousQuestions = query
                .where(entity.order.lt(indexQuery.where(entity.question.id.eq(questionId)).fetchFirst())
                        .and(entity.exam.id.eq(examId)))
                .fetchCount();
        var reminder = countPreviousQuestions % pageSize;
        if (reminder == 0) {
            return (int) (countPreviousQuestions / pageSize + 1);
        }
        return (int) Math.ceil((double) countPreviousQuestions / pageSize);
    }
}
