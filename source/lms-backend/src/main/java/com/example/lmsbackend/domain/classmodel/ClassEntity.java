package com.example.lmsbackend.domain.classmodel;

import com.example.lmsbackend.domain.coursemodel.CourseEntity;
import com.example.lmsbackend.domain.discussion.PostEntity;
import com.example.lmsbackend.domain.scheduler.ClassSessionEntity;
import com.example.lmsbackend.enums.ClassStatus;
import com.example.lmsbackend.enums.ClassType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "class")
@Data
@EqualsAndHashCode(exclude = {"course", "content"})
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(
        name = "class-sessions",
        attributeNodes = {
                @NamedAttributeNode(value = "sessions")
        }
)
@NamedEntityGraph(
        name = "class-posts",
        attributeNodes = {
                @NamedAttributeNode(value = "posts")
        }
)
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "code")
    @NotNull
    private String code;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ClassStatus status;

    @Column(name = "days_of_week")
    private String daysOfWeek;

    @Column(name = "started_at")
    private LocalDate startedAt;

    @Column(name = "ended_at")
    private LocalDate endedAt;

    @Enumerated(EnumType.STRING)
    private ClassType type;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @OneToOne(cascade = CascadeType.ALL, fetch = LAZY, mappedBy = "classEntity", optional = false)
    private ClassLearningContentEntity learningContent;

    @OneToMany(fetch = LAZY, cascade = CascadeType.ALL, mappedBy = "classEntity")
    private Set<ClassTeacherEntity> teachers = new HashSet<>();

    @OneToMany(fetch = LAZY, cascade = CascadeType.ALL, mappedBy = "classEntity")
    private Set<ClassSessionEntity> sessions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classEntity")
    private Set<PostEntity> posts = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void setCourse(CourseEntity course) {
        this.course = course;
        course.getClasses().add(this);
    }
}
