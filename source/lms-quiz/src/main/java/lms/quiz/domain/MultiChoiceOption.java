package lms.quiz.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class MultiChoiceOption implements Serializable {

    private UUID key;
    private String content;
    private Boolean isCorrect;
    private Long order;
}
