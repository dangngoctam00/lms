package com.example.lmsbackend.domain.course;

import com.example.lmsbackend.domain.coursemodel.CourseEntity;
import com.example.lmsbackend.domain.resource.TextbookEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@FieldNameConstants
@Entity
@Table(name = "course_textbook")
@Setter
@Getter
public class CourseTextbookEntity {

    @EmbeddedId
    private CourseTextbookKey id = new CourseTextbookKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "textbook_id")
    @MapsId("textbookId")
    private TextbookEntity textbook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @MapsId("courseId")
    private CourseEntity course;
}
