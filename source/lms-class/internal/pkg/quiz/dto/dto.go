package dto

import (
	"lms-class/internal/pkg/question/dto"
	"time"
)

type QuizDto struct {
	ID                       int        `json:"id,omitempty"`
	Title                    string     `json:"title,omitempty"`
	Description              string     `json:"description,omitempty"`
	GradeTag                 string     `json:"gradeTag,omitempty"`
	ExamId                   *int       `json:"examId,omitempty"`
	IsPublished              bool       `json:"state,omitempty"`
	CreatedAt                *time.Time `json:"createdAt,omitempty"`
	UpdatedAt                *time.Time `json:"updatedAt,omitempty"`
	Context                  string     `json:"context,omitempty"`
	ContextId                int        `json:"contextId,omitempty"`
	ParentId                 *int       `json:"parentId,omitempty"`
	StartedAt                *time.Time `json:"startedAt,omitempty"`
	FinishedAt               *time.Time `json:"finishedAt,omitempty"`
	TimeLimit                *int       `json:"timeLimit,omitempty"`
	MaxAttempt               *int       `json:"maxAttempt,omitempty"`
	ViewPreviousSessions     bool       `json:"viewPreviousSessions,omitempty"`
	ViewPreviousSessionsTime *time.Time `json:"viewPreviousSessionsTime,omitempty"`
	PassedScore              *int       `json:"passedScore,omitempty"`
	FinalGradedStrategy      *string    `json:"finalGradedStrategy,omitempty"`
	ViewResult               bool       `json:"viewResult,omitempty"`
}

type QuizSession struct {
	ID        int                       `json:"id,omitempty"`
	HasGraded bool                      `json:"hasGraded,omitempty"`
	HasActive bool                      `json:"hasActive,omitempty"`
	Score     int                       `json:"score,omitempty"`
	Questions []dto.QuestionQuizSession `json:"questions"`
}

func (q *QuizDto) WithId(id *int) {
	if id == nil {
		return
	}
	q.ID = *id
}
