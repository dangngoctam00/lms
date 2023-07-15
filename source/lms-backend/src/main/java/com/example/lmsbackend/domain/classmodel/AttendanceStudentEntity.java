package com.example.lmsbackend.domain.classmodel;

import com.example.lmsbackend.enums.AttendanceState;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "attendance_student")
@FieldNameConstants
@Getter
@Setter
@NamedEntityGraph(
        name = "attendance-student",
        attributeNodes = {
                @NamedAttributeNode(value = "attendanceTime", subgraph = "attendance-sub"),
                @NamedAttributeNode(value = "student", subgraph = "student-sub")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "attendance-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "session")
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
public class AttendanceStudentEntity {

    @EmbeddedId
    private StudentAttendanceKey id = new StudentAttendanceKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("attendanceTimeId")
    @JoinColumn(name = "attendance_time_id")
    private SessionAttendanceTimeEntity attendanceTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumns({
            @JoinColumn(name = "student_id", referencedColumnName = "student_id"),
            @JoinColumn(name = "class_id", referencedColumnName = "class_id")
    })
    private ClassStudentEntity student;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @NotNull
    private AttendanceState state = AttendanceState.NONE;

    public void setAttendanceTime(SessionAttendanceTimeEntity attendance) {
        this.attendanceTime = attendance;
        attendance.getAttendances().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceStudentEntity that = (AttendanceStudentEntity) o;
        return Objects.equals(id, that.id) && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state);
    }
}
