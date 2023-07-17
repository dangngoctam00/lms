package lms.quiz.domain;

import lms.quiz.enums.ExamContext;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exam")
@Getter
@Setter
@Audited
public class ExamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "context")
    @NotNull
    private ExamContext context;

    @Column(name = "context_id")
    @NotNull
    private Long contextId;

    @Column(name = "is_publised")
    private Boolean isPublished;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "exam_id")
//    @AuditJoinTable(name = "Exam_Question_AUD", inverseJoinColumns = @JoinColumn(name = "question_id"))
//    private Set<QuizQuestionEntity> questions = new HashSet<>();
}
