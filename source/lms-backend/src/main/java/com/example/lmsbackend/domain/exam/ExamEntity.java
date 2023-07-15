package com.example.lmsbackend.domain.exam;


import com.example.lmsbackend.domain.base.BaseAuditEntity;
import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.domain.coursemodel.CourseEntity;
import com.example.lmsbackend.domain.coursemodel.ExamTextBookEntity;
import com.example.lmsbackend.domain.coursemodel.QuizCourseEntity;
import com.example.lmsbackend.enums.ExamState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@EqualsAndHashCode(exclude = {"questionSources", "course"})
@Table(name = "exam")
@FieldNameConstants
@NamedEntityGraph(
        name = "exam-questionSources",
        attributeNodes = {
                @NamedAttributeNode(value = "questions"),
                @NamedAttributeNode(value = "course")
        }
)
@NamedEntityGraph(
        name = "exam-course",
        attributeNodes = {
                @NamedAttributeNode(value = "course")
        }
)
@NamedEntityGraph(
        name = "exam-quizzes",
        attributeNodes = {
                @NamedAttributeNode(value = "quizzes")
        }
)
public class ExamEntity extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ExamState state;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @OneToMany(
            mappedBy = "exam",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("order ASC")
    private Set<ExamQuestionSourceEntity> questions = new HashSet<>();

    @OneToMany(fetch = LAZY, mappedBy = "exam", cascade = {PERSIST, MERGE})
    private Set<QuizClassEntity> quizzes = new HashSet<>();

    @OneToMany(fetch = LAZY, mappedBy = "exam", cascade = {PERSIST, MERGE})
    private Set<QuizCourseEntity> quizzesCourse = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "exam")
    private Set<ExamTextBookEntity> textbooks = new HashSet<>();

    public void setCourse(CourseEntity course) {
        this.course = course;
        course.getExams().add(this);
    }
}
