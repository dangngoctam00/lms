package com.example.lmsbackend.domain.exam;

import com.example.lmsbackend.domain.exam.base_question.QuestionType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamEntityDto implements Serializable {
    private final String title;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Long createdBy;
    private final Long updatedBy;
    private final List<ExamQuestionSourceEntityDto> questionSources;

    @Data
    public static class ExamQuestionSourceEntityDto implements Serializable {
        private final QuestionSourceEntityDto questionSource;
        private final Integer order;

        @Data
        public static class QuestionSourceEntityDto implements Serializable {
            private final Long id;
            private final QuestionEntityDto questionEntity;

            @Data
            public static class QuestionEntityDto implements Serializable {
                private final Long id;
                private final Long point;
                private final String description;
                private final String attachment;
                private final String note;
                private final QuestionType type;
            }
        }
    }
}
