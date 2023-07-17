package lms.quiz.domain;

import lms.quiz.enums.DraftContext;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "draft")
@Getter
@Setter
public class DraftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "revision_number")
    private Integer revisionNumber;

    @NotNull
    @Column(name = "context")
    private DraftContext context;

    @NotNull
    @Column(name = "context_id")
    private Long contextId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
