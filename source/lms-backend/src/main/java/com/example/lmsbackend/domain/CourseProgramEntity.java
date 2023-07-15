package com.example.lmsbackend.domain;

import com.example.lmsbackend.domain.coursemodel.CourseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "course_program")
@Data
public class CourseProgramEntity {

    @EmbeddedId
    private CourseProgramKey id = new CourseProgramKey();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("programId")
    @JoinColumn(name = "program_id")
    @JsonIgnore
    private ProgramEntity program;


    @Column(name = "sort_order")
    private Integer order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseProgramEntity that = (CourseProgramEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public CourseProgramEntity() {
    }

    public CourseProgramEntity(CourseEntity course, ProgramEntity program, Integer order) {
        this.course = course;
        this.program = program;
        this.order = order;
    }
}
