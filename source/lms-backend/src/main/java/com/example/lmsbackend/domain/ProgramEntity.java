package com.example.lmsbackend.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "program")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"courses"})
@FieldNameConstants
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(
        name = "program-course",
        attributeNodes = {
                @NamedAttributeNode(value = "courses", subgraph = "courses-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "courses-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("course")
                        }
                )
        }
)
public class ProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "is_strict", columnDefinition = "boolean default False")
    private Boolean isStrict;

    @Column(name = "is_published", columnDefinition = "boolean default False")
    private Boolean isPublished;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "program", orphanRemoval = true)
    @org.hibernate.annotations.OrderBy(clause = "order ASC")
    private Set<CourseProgramEntity> courses = new HashSet<>();

    public void addCourse(CourseProgramEntity course) {
        this.courses.add(course);
        course.setProgram(this);
    }

    public void setCourses(Set<CourseProgramEntity> courses) {
        this.courses.clear();
        this.courses.addAll(courses);
    }

    public ProgramEntity() {
    }

    public ProgramEntity(Long id,
                         String name,
                         String code,
                         String description,
                         Boolean isStrict,
                         Boolean isPublished,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         Set<CourseProgramEntity> courses) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.isStrict = isStrict;
        this.isPublished = isPublished;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.courses = courses;
    }
}
