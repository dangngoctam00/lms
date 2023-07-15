package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.course.CourseTextbookEntity;
import com.example.lmsbackend.domain.course.CourseTextbookKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseTextbookRepository extends JpaRepository<CourseTextbookEntity, CourseTextbookKey> {

    @Query("SELECT COUNT(c) FROM CourseTextbookEntity c WHERE c.course.id = ?1")
    Long countTextbook(Long courseId);
}
