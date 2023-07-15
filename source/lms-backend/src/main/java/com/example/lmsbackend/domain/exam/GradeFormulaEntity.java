package com.example.lmsbackend.domain.exam;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "grade_formula")
@Data
public class GradeFormulaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String formula;
    private String expression;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_result_id", unique = true)
    private GradeTag tagResult;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "formula_use_grade_tag",
            joinColumns = @JoinColumn(name = "formula_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<GradeTag> useTags;
}
