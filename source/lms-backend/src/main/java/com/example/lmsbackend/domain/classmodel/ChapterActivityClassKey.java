package com.example.lmsbackend.domain.classmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChapterActivityClassKey implements Serializable {

    private static final long serialVersionUID = 3350319213564744688L;

    @Column(name = "activity_id")
    private Long activityId;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "chapter_id")
    private Long chapterId;

    public ChapterActivityClassKey(Long activityId) {
        this.activityId = activityId;
    }

    public ChapterActivityClassKey(Long activityId, String activityType) {
        this.activityId = activityId;
        this.activityType = activityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChapterActivityClassKey that = (ChapterActivityClassKey) o;
        return Objects.equals(activityId, that.activityId) && Objects.equals(activityType, that.activityType) && Objects.equals(chapterId, that.chapterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, activityType, chapterId);
    }
}
