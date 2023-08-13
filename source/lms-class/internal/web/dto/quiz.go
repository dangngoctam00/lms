package dto

import (
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
}

func (q *QuizDto) WithId(id *int) {
	if id == nil {
		return
	}
	q.ID = *id
}
