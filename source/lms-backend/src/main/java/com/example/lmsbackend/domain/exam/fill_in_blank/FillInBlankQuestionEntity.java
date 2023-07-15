package com.example.lmsbackend.domain.exam.fill_in_blank;

import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quiz_fill_in_blank_question")
@Getter
@Setter
@FieldNameConstants
@NamedEntityGraph(
        name = "fill-in-blank-blanks",
        attributeNodes = {
                @NamedAttributeNode("blanks"),
        }
)
public class FillInBlankQuestionEntity extends Question {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    private Set<FillInBlankOptionEntity> blanks = new HashSet<>();

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private QuestionEntity question;

    @Override
    public int hashCode() {
        return 13;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FillInBlankQuestionEntity other = (FillInBlankQuestionEntity) obj;
        return getId() != null && getId().equals(other.getId());
    }
}