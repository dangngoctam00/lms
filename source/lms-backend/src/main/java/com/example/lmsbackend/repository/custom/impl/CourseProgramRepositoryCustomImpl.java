package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.domain.CourseProgramEntity;
import com.example.lmsbackend.domain.ProgramEntity;
import com.example.lmsbackend.domain.QCourseProgramEntity;
import com.example.lmsbackend.repository.custom.CourseProgramRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
public class CourseProgramRepositoryCustomImpl implements CourseProgramRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public List<CourseProgramEntity> getFetchCourseByProgramIdIn(List<Long> programsId) {
        var courseProgramEntity = QCourseProgramEntity.courseProgramEntity;
        return new BlazeJPAQuery<ProgramEntity>(entityManager, criteriaBuilderFactory)
                .from(courseProgramEntity)
                .leftJoin(courseProgramEntity.course)
                .fetchJoin()
                .where(courseProgramEntity.program.id.in(programsId))
                .select(courseProgramEntity)
                .fetch();
    }
}
// *