package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.ClassLearningContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ClassLearningContentRepository extends JpaRepository<ClassLearningContentEntity, Long> {

    @Query("SELECT c FROM ClassLearningContentEntity c LEFT JOIN FETCH c.chapters WHERE c.id IN ?1")
    Set<ClassLearningContentEntity> findFetchChaptersByClassIn(List<Long> idList);
}
