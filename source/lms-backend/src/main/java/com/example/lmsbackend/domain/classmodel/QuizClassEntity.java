package com.example.lmsbackend.domain.classmodel;

import com.example.lmsbackend.domain.coursemodel.QuizCourseEntity;
import com.example.lmsbackend.domain.exam.ExamEntity;
import com.example.lmsbackend.domain.exam.GradeTag;
import com.example.lmsbackend.domain.exam.QuizConfigEntity;
import com.example.lmsbackend.domain.exam.QuizResultEntity;
import com.example.lmsbackend.enums.ActivityState;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "quiz_class")
@FieldNameConstants
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(
        name = "quiz-class-config",
        attributeNodes = {
                @NamedAttributeNode(value = "config")
        }
)
@NamedEntityGraph(
        name = "quiz-class-config-exam",
        attributeNodes = {
                @NamedAttributeNode(value = "config"),
                @NamedAttributeNode(value = "exam", subgraph = "quiz-class-config-exam-sub")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "quiz-class-config-exam-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "course")
                        }
                )
        }
)
@NamedEntityGraph(
        name = "quiz-class-exam",
        attributeNodes = {
                @NamedAttributeNode(value = "exam")
        }
)
public class QuizClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id")
    private GradeTag tag;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "exam_id")
    private ExamEntity exam;

    @OneToOne(fetch = LAZY, mappedBy = "quiz", optional = false)
    private QuizResultEntity result;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ActivityState state = ActivityState.PRIVATE;

    @OneToOne(fetch = LAZY, mappedBy = "quiz", optional = false, cascade = CascadeType.ALL)
    private QuizConfigEntity config;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "quiz_course_id")
    private QuizCourseEntity quizCourse;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "class_id")
    @NotNull
    private ClassEntity classEntity;

    @Transient
    private Integer order;

    @Column(name = "create_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    public void setExam(ExamEntity exam) {
        this.exam = exam;
        exam.getQuizzes().add(this);
    }

    public void removeExam() {
        if (exam != null) {
            exam.getQuizzes().remove(this);
            this.exam = null;
        }
    }

    public void setQuizCourse(QuizCourseEntity quizCourse) {
        this.quizCourse = quizCourse;
        quizCourse.getQuizzesClass().add(this);
    }
}
