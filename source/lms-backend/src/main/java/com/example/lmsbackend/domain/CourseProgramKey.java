package com.example.lmsbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseProgramKey implements Serializable {
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "program_id")
    private Long programId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseProgramKey that = (CourseProgramKey) o;
        return Objects.equals(courseId, that.courseId) && Objects.equals(programId, that.programId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, programId);
    }
}
