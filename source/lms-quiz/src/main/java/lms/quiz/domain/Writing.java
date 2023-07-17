package lms.quiz.domain;

public class Writing extends QuestionData {

    public static final String TYPE = "WRITING";

    @Override
    public String getType() {
        return TYPE;
    }
}
