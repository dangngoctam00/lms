package com.example.lmsbackend.domain.scheduler;

import com.example.lmsbackend.domain.StaffEntity;
import com.example.lmsbackend.domain.base.BaseAuditEntity;
import com.example.lmsbackend.domain.classmodel.ClassEntity;
import com.example.lmsbackend.domain.classmodel.SessionAttendanceTimeEntity;
import com.example.lmsbackend.domain.classmodel.UnitClassEntity;
import com.example.lmsbackend.enums.SessionAttendanceStrategy;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "class_session")
@FieldNameConstants
@Getter
@Setter
public class ClassSessionEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teacher_id")
    private StaffEntity teacher;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_id")
    private UnitClassEntity unit;

    @Column(name = "is_scheduled")
    private Boolean isScheduled;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "room")
    private String room;

    @Column(name = "note")
    private String note;

    @Column(name = "note_in_session")
    private String noteInSession;

    @Column(name = "strategy")
    @Enumerated(EnumType.STRING)
    @NotNull
    private SessionAttendanceStrategy strategy = SessionAttendanceStrategy.LAST_TIME;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SessionAttendanceTimeEntity> attendancesTime = new HashSet<>();

    public void setUnit(UnitClassEntity unit) {
        this.unit = unit;
        unit.getSessions().add(this);
    }

    public void setClassEntity(ClassEntity classEntity) {
        this.classEntity = classEntity;
        classEntity.getSessions().add(this);
    }

    public void removeUnit(UnitClassEntity unit) {
        if (this.unit != null) {
            unit.getSessions().remove(this);
            this.unit = null;
        }
    }
}
