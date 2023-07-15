package com.example.lmsbackend.domain.coursemodel;

import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.domain.exam.ExamEntity;
import com.example.lmsbackend.domain.exam.GradeTag;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "quiz_course")
public class QuizCourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id")
    private GradeTag tag;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private ExamEntity exam;

    @JoinColumn(name = "course_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quizCourse", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<QuizClassEntity> quizzesClass = new HashSet<>();

    @Transient
    private Integer order;

    public void setExam(ExamEntity exam) {
        this.exam = exam;
        exam.getQuizzesCourse().add(this);
    }

    public void removeExam() {
        if (exam != null) {
            exam.getQuizzes().remove(this);
            this.exam = null;
        }
    }
}
