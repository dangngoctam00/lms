package dto

import (
	"encoding/json"
	"lms-class/ent"
	"time"
)

type QuestionDto struct {
	ID           int         `json:"id"`
	Context      string      `json:"context"`
	ContextId    int         `json:"contextId"`
	Position     int         `json:"position"`
	QuestionType string      `json:"questionType"`
	Data         interface{} `mapper:"-"`
	UpdatedAt    time.Time   `json:"updatedAt"`
}

type QuestionData struct {
	Description string       `json:"description"`
	Point       int          `json:"point"`
	Attachments []Attachment `json:"attachments"`
}

type WritingQuestion struct {
	QuestionData
}

type MultiChoiceQuestion struct {
	QuestionData
	IsMultipleAnswer bool                `json:"isMultipleAnswer"`
	Options          []MultiChoiceOption `json:"options"`
}

type MultiChoiceOption struct {
	Key       string `json:"key"`
	Content   string `json:"content"`
	IsCorrect bool   `json:"isCorrect"`
	Order     int    `json:"order"`
}

type Attachment struct {
	Url         string `json:"url"`
	ContentType string `json:"contentType"`
}

const (
	Writing     = "WRITING"
	MultiChoice = "MULTI_CHOICE"
)

const (
	Exam   = "EXAM"
	Course = "COURSE"
)

type Question ent.Question

func (s *QuestionDto) SetData(source ent.Question) {
	s.DoSetData(source.QuestionType, source.Data)
}

func (s *QuestionDto) DoSetData(questionType string, questionData json.RawMessage) {
	switch questionType {
	case Writing:
		res := &WritingQuestion{}
		err := json.Unmarshal(questionData, res)
		if err != nil {
			//return nil
		}
		s.Data = *res
	case MultiChoice:
		res := &MultiChoiceQuestion{}
		err := json.Unmarshal(questionData, res)
		if err != nil {
			//return nil
		}
		s.Data = *res
	}
}
