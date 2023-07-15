package com.example.lmsbackend.domain.coursemodel;

import com.example.lmsbackend.domain.compositekey.ExamTextBookKey;
import com.example.lmsbackend.domain.exam.ExamEntity;
import com.example.lmsbackend.domain.resource.TextbookEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "exam_textbook")
@Setter
@Getter
public class ExamTextBookEntity {

    @EmbeddedId
    private ExamTextBookKey id = new ExamTextBookKey();

    @ManyToOne
    @MapsId("examId")
    @JoinColumn(name = "exam_id")
    private ExamEntity exam;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("textbookId")
    @JoinColumn(name = "textbook_id")
    private TextbookEntity textbook;

    @Column(name = "note")
    private String note;

    public void setTextbook(TextbookEntity textbook) {
        this.textbook = textbook;
        textbook.getExams().add(this);
    }

    public void setExam(ExamEntity exam) {
        this.exam = exam;
        exam.getTextbooks().add(this);
    }
}
