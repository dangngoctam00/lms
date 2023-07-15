package com.example.lmsbackend.domain.exam;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class GradeTagStudentKey implements Serializable {

    private static final long serialVersionUID = 812624376483835647L;
    private Long tag;

    private Long student;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeTagStudentKey that = (GradeTagStudentKey) o;
        return Objects.equals(tag, that.tag) && Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, student);
    }
}
