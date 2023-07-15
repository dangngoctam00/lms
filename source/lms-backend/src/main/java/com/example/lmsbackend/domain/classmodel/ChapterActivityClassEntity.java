package com.example.lmsbackend.domain.classmodel;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "chapter_activity_class")
@Getter
@Setter
public class ChapterActivityClassEntity {

    @EmbeddedId
    private ChapterActivityClassKey id;

    @Column(name = "sort_index")
    @NotNull
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chapterId")
    public ChapterClassEntity chapter;

    public void setChapter(ChapterClassEntity chapter) {
        this.chapter = chapter;
        chapter.getActions().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChapterActivityClassEntity that = (ChapterActivityClassEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }
}
