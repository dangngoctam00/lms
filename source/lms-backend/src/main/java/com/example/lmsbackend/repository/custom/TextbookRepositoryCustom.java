package com.example.lmsbackend.repository.custom;

import com.blazebit.persistence.PagedList;
import com.example.lmsbackend.domain.resource.TextbookEntity;

public interface TextbookRepositoryCustom {
    PagedList<TextbookEntity> getTextBooks(String keyword, Integer page, Integer size);

    PagedList<TextbookEntity> getTextbooksByCourse(Long courseId, String keyword, Integer page, Integer size);
}
