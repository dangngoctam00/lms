package com.example.lmsbackend.domain.classmodel;

import com.example.lmsbackend.domain.compositekey.UnitTextBookKey;
import com.example.lmsbackend.domain.resource.TextbookEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Entity
@Table(name = "unit_class_text_book")
@Setter
@Getter
@EqualsAndHashCode(exclude = {"unit", "textbook"})
@FieldNameConstants
public class UnitClassTextBookEntity {

    @EmbeddedId
    private UnitTextBookKey id = new UnitTextBookKey();

    @ManyToOne
    @MapsId("unitId")
    @JoinColumn(name = "unit_id")
    private UnitClassEntity unit;

    @ManyToOne
    @MapsId("textbookId")
    @JoinColumn(name = "textbook_id")
    private TextbookEntity textbook;

    @Column(name = "note")
    private String note;

    public void setTextbook(TextbookEntity textbook) {
        this.textbook = textbook;
        textbook.getUnitsClasses().add(this);
    }

    public void setUnit(UnitClassEntity unit) {
        this.unit = unit;
        unit.getTextbooks().add(this);
    }
}
