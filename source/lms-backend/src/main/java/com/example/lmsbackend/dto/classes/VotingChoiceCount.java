package com.example.lmsbackend.dto.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingChoiceCount {
    private Long choiceId;
    private Long count;
}
