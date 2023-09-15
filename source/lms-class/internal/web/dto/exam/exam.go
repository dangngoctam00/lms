package exam

import (
	"lms-class/internal/web/dto/question"
	"time"
)

type ExamDto struct {
	Id          int                    `json:"id"`
	Title       string                 `json:"title"`
	Context     string                 `json:"context"`
	ContextId   string                 `json:"contextId"`
	IsPublished bool                   `json:"isPublished"`
	Questions   []question.QuestionDto `json:"questions" mapper:"-"`
	UpdatedAt   time.Time              `json:"updatedAt"`
}
