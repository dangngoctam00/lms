package com.example.lmsbackend.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SortCriterion {
    @NotNull
    private String field;
    @NotNull
    private String direction;
}
