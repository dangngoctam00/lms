package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.classmodel.ClassStudentEntity;
import com.example.lmsbackend.domain.classmodel.QClassStudentEntity;
import com.example.lmsbackend.dto.classes.MemberDto;
import com.example.lmsbackend.repository.custom.ClassStudentRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.example.lmsbackend.constant.AppConstant.ASC;

@RequiredArgsConstructor
public class ClassStudentRepositoryCustomImpl implements ClassStudentRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public PagedList<MemberDto> getStudents(Long classId, BaseSearchCriteria criteria) {
        var classStudent = QClassStudentEntity.classStudentEntity;
        StringPath firstName = Expressions.stringPath(classStudent.student, "firstName");
        var query = new BlazeJPAQuery<ClassStudentEntity>(entityManager, criteriaBuilderFactory)
                .from(classStudent)
                .select(Projections.constructor(MemberDto.class,
                        firstName,
                        classStudent.student.lastName,
                        classStudent.student.username,
                        classStudent.student.email,
                        classStudent.student.phone,
                        classStudent.student.id,
                        classStudent.student.avatar,
                        classStudent.student.address))
                .where(classStudent.classEntity.id.eq(classId));
        var sort = criteria.getSorts();
        if (CollectionUtils.isNotEmpty(sort)) {
            if (StringUtils.equals(sort.get(0).getDirection(), ASC)) {
                query.orderBy(firstName.asc());
            } else {
                query.orderBy(firstName.desc());
            }
        }
        query.orderBy(classStudent.student.id.asc())
                .orderBy(classStudent.classEntity.id.asc());
        if (StringUtils.isNoneBlank(criteria.getKeyword())) {
            query.where(classStudent.student.lastName.concat(" ").concat(classStudent.student.firstName).containsIgnoreCase(criteria.getKeyword()));
        }
        return query.fetchPage((criteria.getPagination().getPage() - 1) * criteria.getPagination().getSize(), criteria.getPagination().getSize());
    }
}
