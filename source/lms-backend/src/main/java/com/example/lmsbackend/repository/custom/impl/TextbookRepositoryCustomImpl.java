package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.domain.ProgramEntity;
import com.example.lmsbackend.domain.resource.QTextbookEntity;
import com.example.lmsbackend.domain.resource.TextbookEntity;
import com.example.lmsbackend.repository.custom.TextbookRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
public class TextbookRepositoryCustomImpl implements TextbookRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public PagedList<TextbookEntity> getTextBooks(String keyword, Integer page, Integer size) {
        var textbook = QTextbookEntity.textbookEntity;
        return new BlazeJPAQuery<ProgramEntity>(entityManager, criteriaBuilderFactory)
                .from(textbook)
                .where(textbook.name.containsIgnoreCase(keyword))
                .select(textbook)
                .orderBy(textbook.id.desc())
                .fetchPage((page - 1) * size, size);
    }

    @Override
    public PagedList<TextbookEntity> getTextbooksByCourse(Long courseId, String keyword, Integer page, Integer size) {
        return null;
    }
}
