package com.example.lmsbackend.domain.coursemodel;

import com.example.lmsbackend.domain.CourseProgramEntity;
import com.example.lmsbackend.domain.classmodel.ClassEntity;
import com.example.lmsbackend.domain.exam.ExamEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "course")
@EqualsAndHashCode(exclude = {"programs", "classes", "content", "exams"})
@FieldNameConstants
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(
        name = "course-full",
        attributeNodes = {
                @NamedAttributeNode(value = "content", subgraph = "content-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "content-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "chapters", subgraph = "chapters-sub")
                        }
                ),
                @NamedSubgraph(
                        name = "chapters-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "chaptersClass")
                        }
                )
        }
)
@NamedEntityGraph(
        name = "course-program",
        attributeNodes = {
                @NamedAttributeNode(value = "programs")
        }
)
@NamedEntityGraph(
        name = "course-exams",
        attributeNodes = {
                @NamedAttributeNode(value = "exams")
        }
)
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", updatable = false, unique = true, nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "level", nullable = false)
    @NotBlank
    private String level;

    @Column(name = "background")
    private String background;

    @Column(name = "created_at", nullable = false, updatable = false)
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

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY, optional = false, mappedBy = "course")
    private CourseLearningContentEntity content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
    private Set<CourseProgramEntity> programs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    private Set<ClassEntity> classes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.REMOVE)
    private Set<ExamEntity> exams = new HashSet<>();

    public void removeExam(ExamEntity exam) {
        exams.remove(exam);
    }

    public void addProgram(CourseProgramEntity program) {
        this.programs.add(program);
        program.setCourse(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseEntity that = (CourseEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(code, that.code) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(background, that.background) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, description, price, background, createdAt, updatedAt);
    }
}