package lms.quiz.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                name = Writing.TYPE,
                value = Writing.class
        ),
        @JsonSubTypes.Type(
                name = MultiChoice.TYPE,
                value = MultiChoice.class
        ),
})
@Getter
@Setter
public abstract class QuestionData implements Serializable {

    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;

    private Long point;

    private String note;

    private List<Attachment> attachments = new ArrayList<>();

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    public abstract String getType();
}
