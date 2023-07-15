package com.example.lmsbackend.domain.course;

import com.example.lmsbackend.domain.coursemodel.ChapterCourseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "chapter_activity_course")
@Getter
@Setter
public class ChapterActivityCourseEntity {

    @EmbeddedId
    private ChapterActivityCourseKey id;

    @Column(name = "sort_index")
    @NotNull
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chapterId")
    public ChapterCourseEntity chapter;

    public void setChapter(ChapterCourseEntity chapter) {
        this.chapter = chapter;
        chapter.getActions().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChapterActivityCourseEntity that = (ChapterActivityCourseEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }
}
