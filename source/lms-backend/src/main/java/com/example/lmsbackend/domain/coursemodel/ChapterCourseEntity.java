package com.example.lmsbackend.domain.coursemodel;

import com.example.lmsbackend.domain.classmodel.ChapterClassEntity;
import com.example.lmsbackend.domain.course.ChapterActivityCourseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "chapter_course")
public class ChapterCourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "sort_index")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_content_id")
    @JsonIgnore
    private CourseLearningContentEntity courseContent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chapterCourse")
    private Set<ChapterClassEntity> chaptersClass = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chapter", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("order ASC")
    private Set<ChapterActivityCourseEntity> actions = new HashSet<>();

    public void setCourseContent(CourseLearningContentEntity courseContent) {
        this.courseContent = courseContent;
        courseContent.getChapters().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChapterCourseEntity chapter = (ChapterCourseEntity) o;
        return Objects.equals(id, chapter.id) && Objects.equals(title, chapter.title) && Objects.equals(order, chapter.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, order);
    }
}
