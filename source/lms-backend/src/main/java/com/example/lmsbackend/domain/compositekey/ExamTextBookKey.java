package com.example.lmsbackend.domain.compositekey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamTextBookKey implements Serializable {
    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "textbook_id")
    private Long textbookId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExamTextBookKey that = (ExamTextBookKey) o;
        return Objects.equals(examId, that.examId) && Objects.equals(textbookId, that.textbookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(examId, textbookId);
    }
}
