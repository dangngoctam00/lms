package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.coursemodel.UnitCourseEntity;
import com.example.lmsbackend.dto.response.course.UnitDto;
import com.example.lmsbackend.repository.custom.UnitCourseRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UnitCourseRepository extends JpaRepository<UnitCourseEntity, Long>, UnitCourseRepositoryCustom {

    @Query("SELECT new com.example.lmsbackend.dto.response.course.UnitDto(u.id, u.title) FROM UnitCourseEntity u WHERE u.id IN ?1")
    List<UnitDto> findUnitsDtoByIdIn(List<Long> idList);

    @Query("SELECT u FROM UnitCourseEntity u LEFT JOIN FETCH u.textbooks WHERE u.id IN ?1")
    List<UnitCourseEntity> findFetchTextbookUnitsDtoByIdIn(List<Long> idList);

    @Query("SELECT u FROM UnitCourseEntity u WHERE u.id IN ?1")
    List<UnitCourseEntity> findUnitsByIdIn(List<Long> idList);

    @Query("SELECT u FROM UnitCourseEntity u LEFT JOIN FETCH u.textbooks WHERE u.id = ?1 ORDER BY u.id")
    Optional<UnitCourseEntity> findFetchById(Long id);

    @Query("SELECT u FROM UnitCourseEntity u LEFT JOIN FETCH u.textbooks WHERE u.course.id = ?1 ORDER BY u.id ASC")
    Set<UnitCourseEntity> findFetchByCourseId(Long courseId);

    @Query("SELECT u.title FROM UnitCourseEntity u WHERE u.id = ?1")
    String getNameById(Long id);
}
