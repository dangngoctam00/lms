package com.example.lmsbackend.domain.classmodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@EqualsAndHashCode(exclude = {"classEntity", "chapters"})
@Table(name = "class_learning_content")
@FieldNameConstants
public class ClassLearningContentEntity {

    @Id
    private Long id;

    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private ClassEntity classEntity;

    public void setClassEntity(ClassEntity classEntity) {
        this.classEntity = classEntity;
        classEntity.setLearningContent(this);
    }

    @OneToMany(fetch = LAZY, mappedBy = "learningContent", cascade = CascadeType.ALL)
    private Set<ChapterClassEntity> chapters = new HashSet<>();
}
