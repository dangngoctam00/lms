package com.example.lmsbackend.domain.exam;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "grade_tag")
@Getter
@Setter
public class GradeTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope")
    @NotNull
    private GradeTagScope scope;

    @Column(name = "scope_id")
    @NotNull
    private Long scopeId;

    @Column(name = "is_primitive")
    @NotNull
    private boolean isPrimitive;

    @Column(name = "has_graded")
    @NotNull
    private boolean hasGraded;

    @Column(name = "graded_at")
    private Timestamp gradedAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "is_public")
    private boolean isPublic;

    @OneToOne(mappedBy = "tagResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private GradeFormulaEntity gradeFormula;
}
