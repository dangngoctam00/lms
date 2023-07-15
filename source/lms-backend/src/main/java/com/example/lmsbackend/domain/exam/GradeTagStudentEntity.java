package com.example.lmsbackend.domain.exam;

import com.example.lmsbackend.domain.StudentEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "grade_tag_student",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"tag_id", "student_id"})}
)
@IdClass(GradeTagStudentKey.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeTagStudentEntity {

    @Id
    @JoinColumn(name="tag_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GradeTag tag;

    @Id
    @JoinColumn(name="student_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StudentEntity student;

    @Column(name = "grade")
    private Double grade;
}

