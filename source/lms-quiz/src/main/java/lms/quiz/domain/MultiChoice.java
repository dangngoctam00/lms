package lms.quiz.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class MultiChoice extends QuestionData {

    public static final String TYPE = "MULTI_CHOICE";

    private Boolean isMultipleAnswer;

    private Set<MultiChoiceOption> options = new HashSet<>();

    @Override
    public String getType() {
        return TYPE;
    }
}