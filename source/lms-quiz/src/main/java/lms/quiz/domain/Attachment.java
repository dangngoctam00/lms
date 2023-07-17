package lms.quiz.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Attachment implements Serializable {

    private String url;
    private String contentType;
}
