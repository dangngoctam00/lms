package com.example.lmsbackend.domain.exam;

import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "quiz_config")
@FieldNameConstants
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(
        name = "exam-config-quiz",
        attributeNodes = {
                @NamedAttributeNode(value = "quiz")
        }
)
public class QuizConfigEntity {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "id")
    private QuizClassEntity quiz;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "valid_before")
    private LocalDateTime validBefore;

    @Column(name = "have_time_limit")
    private Boolean haveTimeLimit;

    @Column(name = "time_limit")
    private Long timeLimit;

    @Column(name = "max_attempt")
    private Integer maxAttempt;

    @Column(name = "view_previous_sessions")
    private Boolean viewPreviousSessions;

    @Column(name = "view_previous_sessions_time")
    private LocalDateTime viewPreviousSessionsTime;

    @Column(name = "pass_score")
    private Double passScore;

    @Column(name = "create_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    public void setQuiz(QuizClassEntity quiz) {
        this.quiz = quiz;
        quiz.setConfig(this);
    }
}
