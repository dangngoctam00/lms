package com.example.lmsbackend.domain.classmodel;

import com.example.lmsbackend.domain.coursemodel.ChapterCourseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@Table(name = "chapter_class")
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class ChapterClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "sort_index")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "learning_content_id")
    private ClassLearningContentEntity learningContent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chapter_course_id")
    private ChapterCourseEntity chapterCourse;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chapter", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("order ASC")
    private Set<ChapterActivityClassEntity> actions = new HashSet<>();

    public void setLearningContent(ClassLearningContentEntity learningContent) {
        this.learningContent = learningContent;
        learningContent.getChapters().add(this);
    }

    public void setChapterCourse(ChapterCourseEntity chapterCourse) {
        this.chapterCourse = chapterCourse;
        chapterCourse.getChaptersClass().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ChapterClassEntity))
            return false;

        ChapterClassEntity other = (ChapterClassEntity) o;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
