package com.example.lmsbackend.domain.classmodel;

import com.example.lmsbackend.domain.scheduler.ClassSessionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "attendance")
@FieldNameConstants
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(
        name = "attendance",
        attributeNodes = {
                @NamedAttributeNode(value = "attendances", subgraph = "attendances-sub")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "attendances-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "student", subgraph = "student-sub")
                        }
                ),
                @NamedSubgraph(
                        name = "student-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "student")
                        }
                )
        }
)
public class SessionAttendanceTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attendanceTime", cascade = CascadeType.ALL)
    private Set<AttendanceStudentEntity> attendances = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private ClassSessionEntity session;

    @Column(name = "is_official")
    private Boolean isOfficial = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void setSession(ClassSessionEntity session) {
        this.session = session;
        session.getAttendancesTime().add(this);
    }
}
