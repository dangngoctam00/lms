package com.example.lmsbackend.domain.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseTextbookKey implements Serializable {

    private static final long serialVersionUID = -4405302865512421801L;
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "textbook_id")
    private Long textbookId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseTextbookKey that = (CourseTextbookKey) o;
        return Objects.equals(courseId, that.courseId) && Objects.equals(textbookId, that.textbookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, textbookId);
    }
}
