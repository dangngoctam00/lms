package dto

import (
	"lms-class/internal/pkg/question/dto"
	"time"
)

type ExamDto struct {
	Id          int               `json:"id"`
	Title       string            `json:"title"`
	Context     string            `json:"context"`
	ContextId   string            `json:"contextId"`
	IsPublished bool              `json:"isPublished"`
	Questions   []dto.QuestionDto `json:"questions" mapper:"-"`
	UpdatedAt   time.Time         `json:"updatedAt"`
}
