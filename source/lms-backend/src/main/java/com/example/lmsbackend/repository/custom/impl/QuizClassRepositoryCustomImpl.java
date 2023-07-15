package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.domain.classmodel.QQuizClassEntity;
import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.dto.response.course.QuizDto;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.ActivityState;
import com.example.lmsbackend.repository.custom.QuizClassRepositoryCustom;
import com.example.lmsbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;

@RequiredArgsConstructor
public class QuizClassRepositoryCustomImpl implements QuizClassRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    private final UserService userService;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public Optional<QuizClassEntity> findFetchById(Long id, String properties) {
        var graph = getGraph(properties);
        var cb = entityManager.getCriteriaBuilder();

        var query = cb.createQuery(QuizClassEntity.class);
        var root = query.from(QuizClassEntity.class);
        query.select(root);
        query.where(cb.equal(root.get(QuizClassEntity.Fields.id), id));
        var typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(FETCH_GRAPH, graph);
        var result = typedQuery.getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    private EntityGraph<?> getGraph(String properties) {
        return entityManager.getEntityGraph(String.format("quiz-class-%s", properties));
    }

    @Override
    public List<QuizDto> findQuizzesByIdIn(List<Long> idList) {
        var currentUser = userService.getCurrentUser();
        if (currentUser.getAccountType() == AccountTypeEnum.STUDENT) {
            return entityManager.createQuery("SELECT new com.example.lmsbackend.dto.response.course.QuizDto(q.id, q.title, q.quizCourse.id, q.exam.id, q.state) FROM QuizClassEntity q WHERE q.id IN (:idList) AND q.state = 'PUBLIC'")
                    .setParameter("idList", idList)
                    .getResultList();
        }
        return entityManager.createQuery("SELECT new com.example.lmsbackend.dto.response.course.QuizDto(q.id, q.title, q.quizCourse.id, q.exam.id, q.state) FROM QuizClassEntity q WHERE q.id IN (:idList)")
                .setParameter("idList", idList)
                .getResultList();
    }

    @Override
    public List<QuizClassEntity> findByClassAndRole(Long classId, AccountTypeEnum accountType) {
        var quiz = QQuizClassEntity.quizClassEntity;
        var query = new BlazeJPAQuery<QuizClassEntity>(entityManager, criteriaBuilderFactory)
                .from(quiz)
                .where(quiz.classEntity.id.eq(classId))
                .orderBy(quiz.createdAt.desc());
        if (accountType == AccountTypeEnum.STUDENT) {
            query.where(quiz.state.eq(ActivityState.PUBLIC));
        }
        return query
                .select(quiz)
                .fetch();
    }
}
