package com.example.lmsbackend.domain.classmodel;

import com.example.lmsbackend.domain.coursemodel.UnitCourseEntity;
import com.example.lmsbackend.domain.scheduler.ClassSessionEntity;
import com.example.lmsbackend.enums.ActivityState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "unit_class")
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(
        name = "unit-class",
        attributeNodes = {
                @NamedAttributeNode(value = "textbooks", subgraph = "textbook-sub")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "textbook-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "textbook")
                        }
                )
        }
)
public class UnitClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ActivityState state = ActivityState.PRIVATE;

    @Column(name = "attachment")
    private String attachment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_course_id")
    private UnitCourseEntity unitCourse;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "class_id")
    @NotNull
    private ClassEntity classEntity;

    @Transient
    private Integer order;

    @OneToMany(fetch = LAZY, cascade = CascadeType.ALL, mappedBy = "unit")
    private Set<UnitClassTextBookEntity> textbooks = new HashSet<>();

    @OneToMany(fetch = LAZY, mappedBy = "unit")
    private Set<ClassSessionEntity> sessions = new HashSet<>();

    public void setUnitCourse(UnitCourseEntity unitCourse) {
        this.unitCourse = unitCourse;
        unitCourse.getUnitsClass().add(this);
    }
}
