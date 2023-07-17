package lms.quiz.domain;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lms.quiz.enums.QuizContext;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "quiz_question")
@TypeDef(name = "json", typeClass = JsonType.class)
@Getter
@Setter
@Audited
public class QuizQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "context")
    @NotNull
    private QuizContext context;

    @Column(name = "context_id")
    @NotNull
    private Long contextId;

    @Column(name = "position")
    @NotNull
    private Long position;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private QuizQuestionEntity parent;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "exam_id")
//    private ExamEntity exam;

    @Type(type = "json")
    @Column(name = "data", columnDefinition = "jsonb")
    private QuestionData data;
}
