package com.example.lmsbackend.domain.exam;


import com.example.lmsbackend.domain.exam.base_question.QuestionSourceEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@ToString(exclude = {"exam", "questionSource"})
@RequiredArgsConstructor
@Table(name = "exam_question_source")
@FieldNameConstants
@NamedEntityGraph(
        name = "exam-question-source-question",
        attributeNodes = {
                @NamedAttributeNode(value = "question", subgraph = "exam-question-source-question-question")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "exam-question-source-question-question",
                        attributeNodes = {
                                @NamedAttributeNode("question")
                        }
                )
        }
)
public class ExamQuestionSourceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "exam_id")
    private ExamEntity exam;

    @ManyToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "question_source_id")
    private QuestionSourceEntity question;

    public void setExam(ExamEntity exam) {
        this.exam = exam;
        exam.getQuestions().add(this);
    }

    public void setQuestion(QuestionSourceEntity question) {
        this.question = question;
        question.getExams().add(this);
    }

    @Column(name = "sort_index")
    private Integer order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ExamQuestionSourceEntity that = (ExamQuestionSourceEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
