package com.example.lmsbackend.repository;


import com.example.lmsbackend.domain.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long>, JpaSpecificationExecutor<StudentEntity> {

    @Query("SELECT s.email FROM StudentEntity s INNER JOIN ClassStudentEntity c " +
            "ON s.id = c.id.studentId " +
            "WHERE c.id.classId = ?1 AND s.email IS NOT NULL")
    List<String> findStudentEmailsByClass(Long classId);

    @Query("SELECT s FROM StudentEntity s INNER JOIN ClassStudentEntity c " +
            "ON s.id = c.id.studentId " +
            "WHERE c.id.classId = ?1")
    List<StudentEntity> findStudentByClass(Long classId);

    boolean existsByEmail(String email);

    @Query("select student " +
            "from StudentEntity student " +
            "where lower(concat(concat(student.firstName,' ',student.lastName),student.email,student.phone)) like concat('%',lower(:keyword),'%') " +
            "and student.id not in (select classStudent.student.id from ClassStudentEntity classStudent where classStudent.classEntity.id = :classId)")
    List<StudentEntity> findAllStudentNotInClass(@Param("keyword") String keyword, @Param("classId") Long classId);
}
