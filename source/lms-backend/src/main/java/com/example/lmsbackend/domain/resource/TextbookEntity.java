package com.example.lmsbackend.domain.resource;

import com.example.lmsbackend.domain.classmodel.UnitClassTextBookEntity;
import com.example.lmsbackend.domain.coursemodel.ExamTextBookEntity;
import com.example.lmsbackend.domain.coursemodel.UnitCourseTextBookEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "textbook")
@Getter
@Setter
@FieldNameConstants
@EqualsAndHashCode(exclude = {"units", "exams"})
public class TextbookEntity extends ResourceEntity {

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "author")
    private String author;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "textbook", cascade = CascadeType.REMOVE)
    private Set<UnitCourseTextBookEntity> units = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "textbook", cascade = CascadeType.REMOVE)
    private Set<UnitClassTextBookEntity> unitsClasses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "textbook", cascade = CascadeType.REMOVE)
    private Set<ExamTextBookEntity> exams = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof TextbookEntity)) return false;

        TextbookEntity textbook = (TextbookEntity) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(attachment, textbook.attachment).append(author, textbook.author).append(description, textbook.description).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(attachment).append(author).append(description).toHashCode();
    }
}