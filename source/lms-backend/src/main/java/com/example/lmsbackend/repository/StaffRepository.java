package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.StaffEntity;
import com.example.lmsbackend.repository.custom.StaffRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Long>,
        QuerydslPredicateExecutor<StaffEntity>, StaffRepositoryCustom {
    Optional<StaffEntity> findByUsernameAndIdNot(String username, Long id);

    List<StaffEntity> findAllByUsernameIsNotNullAndIdNot(long staffId);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, long id);

    boolean existsById(Long id);

    @Query("SELECT s.email FROM StaffEntity s INNER JOIN ClassTeacherEntity c " +
            "ON s.id = c.id.teacherId " +
            "WHERE c.id.classId = ?1 AND s.email IS NOT NULL")
    List<String> findTeacherEmailsByClass(Long classId);

    @Query("SELECT s FROM StaffEntity s INNER JOIN ClassTeacherEntity c " +
            "ON s.id = c.id.teacherId " +
            "WHERE c.id.classId = ?1")
    List<StaffEntity> findTeacherByClass(Long classId);
}
