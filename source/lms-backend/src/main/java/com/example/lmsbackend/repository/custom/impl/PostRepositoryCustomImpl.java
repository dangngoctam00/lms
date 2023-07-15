package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.example.lmsbackend.domain.classmodel.ClassEntity;
import com.example.lmsbackend.domain.discussion.PostEntity;
import com.example.lmsbackend.repository.custom.PostRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public Optional<PostEntity> findPostAndInteractionsById(Long postId) {
        var byId = entityManager.createQuery("SELECT p FROM PostEntity p LEFT JOIN FETCH p.interactions WHERE p.id = (:id)", PostEntity.class)
                .setParameter("id", postId)
                .getResultList();
        if (CollectionUtils.isEmpty(byId)) {
            return Optional.empty();
        }
        return Optional.of(byId.get(0));
    }

    @Override
    public Optional<PostEntity> findPostAndCommentsById(Long postId) {
        var byId = entityManager.createQuery("SELECT p FROM PostEntity p LEFT JOIN FETCH p.comments WHERE p.id = (:id)", PostEntity.class)
                .setParameter("id", postId)
                .getResultList();
        if (CollectionUtils.isEmpty(byId)) {
            return Optional.empty();
        }
        return Optional.of(byId.get(0));
    }

    @Override
    public com.example.lmsbackend.repository.custom.PagedList<PostEntity> findPosts(Long classId, Integer page, Integer size, String keyword) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(PostEntity.class);
        var root = query.from(PostEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get(PostEntity.Fields.classEntity).get(ClassEntity.Fields.id), classId));
        if (StringUtils.isNotBlank(keyword)) {
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get(PostEntity.Fields.title)), "%" + keyword.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get(PostEntity.Fields.content)), "%" + keyword.toLowerCase() + "%"))
            );
        }
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.desc(root.get(PostEntity.Fields.updatedAt)));
        query.select(root);
        var typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        typedQuery.setHint(FETCH_GRAPH, entityManager.getEntityGraph("post"));
        var res = typedQuery.getResultList();

        var countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(PostEntity.class)));
        entityManager.createQuery(countQuery);

        countQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        var count = entityManager.createQuery(countQuery)
                .getSingleResult();

        return new com.example.lmsbackend.repository.custom.PagedList<>(res, count, (page - 1) * size, size);
    }
}
