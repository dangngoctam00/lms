package com.example.lmsbackend.repository.custom.utils;

import com.example.lmsbackend.criteria.SortCriterion;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.DESC;

public class BuildJPAQueryCriteria {

    public static <T> void buildSortQuery(List<SortCriterion> sortCriterion, CriteriaBuilder cb, Root<T> root, CriteriaQuery<T> query) {
        sortCriterion
                .forEach(criterion -> buildSortQuery(criterion, cb, root, query));
    }

    private static <T> void buildSortQuery(SortCriterion sortCriterion, CriteriaBuilder cb, Root<T> root, CriteriaQuery<T> query) {
        if (sortCriterion.getDirection().equals(DESC)) {
            query.orderBy(cb.desc(root.get(sortCriterion.getField())));
        } else {
            query.orderBy(cb.asc(root.get(sortCriterion.getField())));
        }
    }
}
