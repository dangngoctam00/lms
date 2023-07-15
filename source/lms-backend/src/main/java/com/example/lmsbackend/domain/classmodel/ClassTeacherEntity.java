package com.example.lmsbackend.domain.classmodel;

import com.example.lmsbackend.domain.StaffEntity;
import com.example.lmsbackend.enums.TeacherRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "class_teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassTeacherEntity {

    @EmbeddedId
    private ClassTeacherKey id = new ClassTeacherKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teacherId")
    @JoinColumn(name = "teacher_id")
    private StaffEntity teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("classId")
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private TeacherRole role;
}
