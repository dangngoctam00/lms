package com.example.lmsbackend.repository.specification;

import com.example.lmsbackend.criteria.FilterCriterion;
import com.example.lmsbackend.domain.StudentEntity;
import com.example.lmsbackend.domain.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {
    private static final String[] FIELD_CAN_SEARCH = {
            UserEntity.Fields.firstName, UserEntity.Fields.lastName, UserEntity.Fields.username, UserEntity.Fields.email, UserEntity.Fields.phone
    };

    public static Specification<StudentEntity> filter(String keyword, List<FilterCriterion> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> predicatesForKeyword = new ArrayList<>();
            if (StringUtils.isNoneBlank(keyword)) {
                for (String field : FIELD_CAN_SEARCH) {
                    predicatesForKeyword.add(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), "%" + keyword.toLowerCase() + "%")
                    );
                }
                predicatesForKeyword.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        criteriaBuilder.concat(
                                                criteriaBuilder.concat(root.get(UserEntity.Fields.lastName), " "), root.get(UserEntity.Fields.firstName)
                                        )
                                ),
                                "%" + keyword.toLowerCase() + "%"
                        )
                );
                predicates.add(criteriaBuilder.or(predicatesForKeyword.toArray(new Predicate[0])));
            }
            for (FilterCriterion filterCriterion : filters) {
                predicates.add(
                        criteriaBuilder.in(root.get(filterCriterion.getField()))
                                .value(filterCriterion.getValues())
                );
            }
            return criteriaBuilder.and((predicates.toArray(new Predicate[0])));
        };
    }
}
