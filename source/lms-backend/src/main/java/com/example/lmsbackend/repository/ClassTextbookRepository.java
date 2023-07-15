package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.ClassTextbookEntity;
import com.example.lmsbackend.domain.classmodel.ClassTextbookKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClassTextbookRepository extends JpaRepository<ClassTextbookEntity, ClassTextbookKey> {

    @Query("SELECT COUNT(c) FROM ClassTextbookEntity c WHERE c.classEntity.id = ?1")
    Long countTextbooks(Long classId);
}
