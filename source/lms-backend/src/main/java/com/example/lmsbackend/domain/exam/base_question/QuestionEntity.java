package com.example.lmsbackend.domain.exam.base_question;

import com.example.lmsbackend.domain.exam.fill_in_blank.FillInBlankQuestionEntity;
import com.example.lmsbackend.domain.exam.fill_in_blank_dnd_question.FillInBlankDragAndDropQuestionEntity;
import com.example.lmsbackend.domain.exam.fill_in_blank_with_choices.FillInBlankMultiChoiceQuestionEntity;
import com.example.lmsbackend.domain.exam.multi_choice.MultiChoiceQuestionEntity;
import com.example.lmsbackend.domain.exam.writing_question.WritingQuestionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "question")
@Getter
@Setter
@FieldNameConstants
@NamedEntityGraph(
        name = "question-questionSource-textbook",
        attributeNodes = {
                @NamedAttributeNode("questionSource")
        }
)
@NamedEntityGraph(
        name = "question-writingQuestion",
        attributeNodes = {
                @NamedAttributeNode(value = "writingQuestion")
        }
)
@NamedEntityGraph(
        name = "question-groupQuestion",
        attributeNodes = {
                @NamedAttributeNode(value = "groupQuestion")
        }
)
@NamedEntityGraph(
        name = "question-multiChoiceQuestion",
        attributeNodes = {
                @NamedAttributeNode(value = "multiChoiceQuestion", subgraph = "multiChoiceQuestion-sub")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "multiChoiceQuestion-sub",
                        attributeNodes = {
                                @NamedAttributeNode("options")
                        }
                )
        }
)
@NamedEntityGraph(
        name = "question-fillInBlankQuestion",
        attributeNodes = {
                @NamedAttributeNode(value = "fillInBlankQuestion", subgraph = "fillInBlankQuestion-sub")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "fillInBlankQuestion-sub",
                        attributeNodes = {
                                @NamedAttributeNode("blanks")
                        }
                )
        }
)
@NamedEntityGraph(
        name = "question-fillInBlankMultiChoiceQuestion",
        attributeNodes = {
                @NamedAttributeNode(value = "fillInBlankMultiChoiceQuestion", subgraph = "fillInBlankMultiChoiceQuestion-sub")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "fillInBlankMultiChoiceQuestion-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "blanks", subgraph = "fillInBlankMultiChoiceQuestion-sub-options")
                        }
                ),
                @NamedSubgraph(
                        name = "fillInBlankMultiChoiceQuestion-sub-options",
                        attributeNodes = {
                                @NamedAttributeNode(value = "options")
                        }
                )
        }
)
@NamedEntityGraph(
        name = "question-fillInBlankDragAndDropQuestion",
        attributeNodes = {
                @NamedAttributeNode(value = "fillInBlankDragAndDropQuestion", subgraph = "fillInBlankDragAndDropQuestion-sub")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "fillInBlankDragAndDropQuestion-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "blanks"),
                                @NamedAttributeNode(value = "answers"),
                        }
                )
        }
)
@NamedEntityGraph(
        name = "question-full",
        attributeNodes = {
                @NamedAttributeNode(value = "writingQuestion"),
                @NamedAttributeNode(value = "fillInBlankQuestion", subgraph = "fillInBlankQuestion-sub"),
                @NamedAttributeNode(value = "multiChoiceQuestion", subgraph = "multiChoiceQuestion-sub"),
                @NamedAttributeNode(value = "fillInBlankDragAndDropQuestion", subgraph = "fillInBlankDragAndDropQuestion-sub"),
                @NamedAttributeNode(value = "fillInBlankMultiChoiceQuestion", subgraph = "fillInBlankMultiChoiceQuestion-sub"),
                @NamedAttributeNode(value = "groupQuestion", subgraph = "groupQuestion-sub"),
                @NamedAttributeNode(value = "questionSource")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "fillInBlankQuestion-sub",
                        attributeNodes = {
                                @NamedAttributeNode("blanks"),
                        }
                ),
                @NamedSubgraph(
                        name = "multiChoiceQuestion-sub",
                        attributeNodes = {
                                @NamedAttributeNode("options"),
                        }
                ),
                @NamedSubgraph(
                        name = "fillInBlankMultiChoiceQuestion-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "blanks", subgraph = "fillInBlankMultiChoiceQuestion-sub-sub"),
                        }
                ),
                @NamedSubgraph(
                        name = "groupQuestion-sub",
                        attributeNodes = {

                        }
                ),
                @NamedSubgraph(
                        name = "fillInBlankMultiChoiceQuestion-sub-sub",
                        attributeNodes = {
                                @NamedAttributeNode("options")
                        }
                ),
                @NamedSubgraph(
                        name = "fillInBlankDragAndDropQuestion-sub",
                        attributeNodes = {
                                @NamedAttributeNode("blanks"),
                                @NamedAttributeNode("answers")
                        }
                ),
        }
)
public class QuestionEntity {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "id")
    private QuestionSourceEntity questionSource;

    @OneToOne(optional = false, fetch = LAZY, mappedBy = "question")
    private WritingQuestionEntity writingQuestion;

    @OneToOne(optional = false, fetch = LAZY, mappedBy = "question")
    private FillInBlankQuestionEntity fillInBlankQuestion;

    @OneToOne(optional = false, fetch = LAZY, mappedBy = "question")
    private MultiChoiceQuestionEntity multiChoiceQuestion;

    @OneToOne(optional = false, fetch = LAZY, mappedBy = "question")
    private FillInBlankMultiChoiceQuestionEntity fillInBlankMultiChoiceQuestion;

    @OneToOne(optional = false, fetch = LAZY, mappedBy = "question")
    private FillInBlankDragAndDropQuestionEntity fillInBlankDragAndDropQuestion;

    @OneToOne(optional = false, fetch = LAZY, mappedBy = "question")
    private GroupQuestionEntity groupQuestion;

    @Column(name = "type")
    private String type;

    @Column(name = "point")
    private Integer point = 0;

    @Column(name = "description")
    private String description;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "note")
    private String note;
}
