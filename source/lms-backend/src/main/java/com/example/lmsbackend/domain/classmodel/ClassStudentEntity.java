package com.example.lmsbackend.domain.classmodel;

import com.example.lmsbackend.domain.StudentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "class_student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassStudentEntity {

    @EmbeddedId
    private ClassStudentKey id = new ClassStudentKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("classId")
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;
}
