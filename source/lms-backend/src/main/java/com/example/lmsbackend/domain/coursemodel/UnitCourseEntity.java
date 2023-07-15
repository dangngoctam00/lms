package com.example.lmsbackend.domain.coursemodel;

import com.example.lmsbackend.domain.classmodel.UnitClassEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "unit_course")
@EqualsAndHashCode(exclude = {"textbooks"})
@NamedEntityGraph(
        name = "unit-textbook",
        attributeNodes = {
                @NamedAttributeNode("textbooks")
        }
)
@NamedEntityGraph(
        name = "unit-course",
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
public class UnitCourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @JoinColumn(name = "course_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @Column(name = "attachment")
    private String attachment;

    @Transient
    private Integer order;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitCourse")
    private Set<UnitClassEntity> unitsClass = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "unit")
    private Set<UnitCourseTextBookEntity> textbooks = new HashSet<>();
}
