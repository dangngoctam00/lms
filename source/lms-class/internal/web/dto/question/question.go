package question

import (
	"encoding/json"
	"github.com/pkg/errors"
	"lms-class/common/xerr"
	"time"
)

type Order int
type Key string

type AnswerQuestion struct {
	Answers []Key `json:"answers"`
}

type QuestionDto struct {
	ID           int        `json:"id,omitempty"`
	Context      string     `json:"context,omitempty"`
	ContextId    int        `json:"contextId,omitempty"`
	Position     int        `json:"position"`
	QuestionType string     `json:"questionType,omitempty"`
	Data         IQuestion  `json:"data,omitempty" mapper:"-"`
	UpdatedAt    *time.Time `json:"updatedAt,omitempty" mapper:"-"`
	Version      int64      `json:"version,omitempty"`
}

type QuestionQuizSession struct {
	ID           int         `json:"id,omitempty"`
	Position     int         `json:"position,omitempty"`
	QuestionType string      `json:"questionType,omitempty"`
	Data         interface{} `json:"data,omitempty" mapper:"-"`
	Answers      []Key       `json:"answers"`
}

func NewQuestionQuizSession(dto QuestionDto) *QuestionQuizSession {
	q := &QuestionQuizSession{}
	q.ID = dto.ID
	q.Position = dto.Position
	q.QuestionType = dto.QuestionType
	q.Data = dto.Data
	return q
}

type IQuestion interface {
	Shuffle()
}

type Data struct {
	Description string       `json:"description"`
	Point       int          `json:"point"`
	Attachments []Attachment `json:"attachments"`
}

type Attachment struct {
	Url         string `json:"url"`
	ContentType string `json:"contentType"`
}

type Answer struct {
	QuestionId int    `json:"questionId"`
	Type       string `json:"type"`
	Values     []Key  `json:"values"`
}

const (
	Writing              = "WRITING"
	MultiChoice          = "MULTI_CHOICE"
	FillInBlank          = "FILL_IN_BLANK"
	DragAndDrop          = "DRAG_AND_DROP"
	BlankWithMultiChoice = "BLANK_MULTI_CHOICE"
)

const (
	Exam   = "EXAM"
	Course = "COURSE"
)

type Question struct {
	ID           int             `json:"id,omitempty"`
	Context      string          `json:"context,omitempty"`
	ContextId    int             `json:"contextId,omitempty"`
	Position     int             `json:"position,omitempty"`
	QuestionType string          `json:"questionType,omitempty"`
	Data         json.RawMessage `json:"data,omitempty"`
	UpdatedAt    time.Time       `json:"updatedAt,omitempty"`
	Version      int64           `json:"version,omitempty"`
}

type MarsalaUnmarshalAction interface {
	GetQuestionData() *IQuestion
	GetQuestionSessionData(q interface{}) (interface{}, error)
}

func (s *QuestionQuizSession) SetData(questionType string, questionData interface{}) error {
	factory := getQuestionObjectFactory(questionType)
	obj, err := factory.GetQuestionSessionData(questionData)
	if err != nil {
		return err
	}
	s.Data = obj
	return nil
}

func (s *QuestionDto) SetQuestionData(questionType string, questionData json.RawMessage) error {
	factory := getQuestionObjectFactory(questionType)
	obj := factory.GetQuestionData()
	err := json.Unmarshal(questionData, obj)
	if err != nil {
		return errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.QuestionDataNotCorrect), "err:%+v", err)
	}
	s.Data = *obj
	return nil
}

func getQuestionObjectFactory(questionType string) MarsalaUnmarshalAction {
	var f MarsalaUnmarshalAction
	switch questionType {
	case Writing:
		f = new(WritingMarsalaUnmarshalAction)
	case MultiChoice:
		f = new(MultiChoiceMarsalaUnmarshalAction)
	case FillInBlank:
		f = new(FillInBlankMarsalaUnmarshalAction)
	case DragAndDrop:
		f = new(DragAndDropMarsalaUnmarshalAction)
	case BlankWithMultiChoice:
		f = new(BlankMultiChoiceMarsalaUnmarshalAction)
	}
	return f
}
