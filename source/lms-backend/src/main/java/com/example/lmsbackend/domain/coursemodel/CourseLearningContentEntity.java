package com.example.lmsbackend.domain.coursemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "course_content")
@EqualsAndHashCode(exclude = {"course", "chapters"})
public class CourseLearningContentEntity {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private CourseEntity course;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "courseContent", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private Set<ChapterCourseEntity> chapters = new HashSet<>();

    public void setCourse(CourseEntity course) {
        this.course = course;
        course.setContent(this);
    }
}
