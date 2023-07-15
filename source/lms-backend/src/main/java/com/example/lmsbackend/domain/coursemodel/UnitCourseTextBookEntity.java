package com.example.lmsbackend.domain.coursemodel;

import com.example.lmsbackend.domain.compositekey.UnitTextBookKey;
import com.example.lmsbackend.domain.resource.TextbookEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "unit_course_text_book")
@Setter
@Getter
public class UnitCourseTextBookEntity {

    @EmbeddedId
    private UnitTextBookKey id = new UnitTextBookKey();

    @ManyToOne
    @MapsId("unitId")
    @JoinColumn(name = "unit_id")
    private UnitCourseEntity unit;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("textbookId")
    @JoinColumn(name = "textbook_id")
    private TextbookEntity textbook;

    @Column(name = "note")
    private String note;

    public void setTextbook(TextbookEntity textbook) {
        this.textbook = textbook;
        textbook.getUnits().add(this);
    }

    public void setUnit(UnitCourseEntity unit) {
        this.unit = unit;
        unit.getTextbooks().add(this);
    }
}
