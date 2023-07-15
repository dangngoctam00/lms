package com.example.lmsbackend.domain.classmodel;

import com.example.lmsbackend.domain.resource.TextbookEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@FieldNameConstants
@Entity
@Table(name = "class_textbook")
@Setter
@Getter
public class ClassTextbookEntity {

    @EmbeddedId
    private ClassTextbookKey id = new ClassTextbookKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "textbook_id")
    @MapsId("textbookId")
    private TextbookEntity textbook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    @MapsId("classId")
    private ClassEntity classEntity;
}
