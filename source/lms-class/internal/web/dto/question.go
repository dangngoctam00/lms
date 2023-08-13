package dto

import (
	"encoding/json"
	"lms-class/ent"
	"math/rand"
	"time"
)

type Order int

type QuestionDto struct {
	ID           int            `json:"id,omitempty"`
	Context      string         `json:"context,omitempty"`
	ContextId    int            `json:"contextId,omitempty"`
	Position     int            `json:"position"`
	QuestionType string         `json:"questionType,omitempty"`
	Data         QuestionAction `json:"data,omitempty" mapper:"-"`
	UpdatedAt    *time.Time     `json:"updatedAt,omitempty" mapper:"-"`
	Version      int64          `json:"version,omitempty"`
}

type QuestionQuizSession struct {
	ID           int         `json:"id,omitempty"`
	Position     int         `json:"position,omitempty"`
	QuestionType string      `json:"questionType,omitempty"`
	Data         interface{} `json:"data,omitempty" mapper:"-"`
}

func NewQuestionQuizSession(dto QuestionDto) *QuestionQuizSession {
	q := &QuestionQuizSession{}
	q.ID = dto.ID
	q.Position = dto.Position
	q.QuestionType = dto.QuestionType
	q.Data = dto.Data
	return q
}

type QuestionAction interface {
	Shuffle()
}

type QuestionData struct {
	Description string       `json:"description"`
	Point       int          `json:"point"`
	Attachments []Attachment `json:"attachments"`
}

type WritingQuestion struct {
	QuestionData
}

func (*WritingQuestion) Shuffle() {
	// Do nothing
}

type MultiChoiceQuestion struct {
	QuestionData
	IsMultipleAnswer bool                `json:"isMultipleAnswer"`
	Options          []MultiChoiceOption `json:"options"`
}

func (q *MultiChoiceQuestion) Shuffle() {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	r.Shuffle(len(q.Options), func(i, j int) {
		q.Options[i], q.Options[j] = q.Options[j], q.Options[i]
	})
	for i := range q.Options {
		q.Options[i].Order = Order(i)
	}
}

type BlankMultiChoiceQuestion struct {
	QuestionData
	Blanks []BlankMultiChoice `json:"blanks"`
}

func (q *BlankMultiChoiceQuestion) Shuffle() {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	for i := range q.Blanks {
		blank := &q.Blanks[i]
		r.Shuffle(len(blank.Options), func(i, j int) {
			blank.Options[i], blank.Options[j] = blank.Options[j], blank.Options[i]
		})
		for i := range blank.Options {
			blank.Options[i].Order = Order(i)
		}
	}
}

type FillInBlankQuestion struct {
	QuestionData
	Blanks []Blank `json:"blanks"`
}

func (*FillInBlankQuestion) Shuffle() {
	// Do nothing
}

type DragAndDropQuestion struct {
	QuestionData
	Blanks  []BlankDragAndDrop  `json:"blanks"`
	Answers []AnswerDragAndDrop `json:"answers"`
}

func (*DragAndDropQuestion) Shuffle() {
	// Do nothing
}

type BlankDragAndDrop struct {
	AnswerKey string `json:"answerKey"`
	Order     Order  `json:"order"`
}

type AnswerDragAndDrop struct {
	AnswerKey string `json:"answerKey"`
	Content   string `json:"content"`
	Order     Order  `json:"order"`
}

type Blank struct {
	ExpectedAnswer string `json:"expectedAnswer"`
	Strategy       string `json:"strategy"`
	Order          Order  `json:"order"`
}

type BlankMultiChoice struct {
	CorrectAnswerKey string        `json:"correctAnswerKey"`
	Options          []BlankOption `json:"options"`
	Order            Order         `json:"order"`
}

type BlankOption struct {
	AnswerKey string `json:"answerKey"`
	Content   string `json:"content"`
	Order     Order  `json:"order"`
}

type MultiChoiceOption struct {
	Key       string `json:"key"`
	Content   string `json:"content"`
	IsCorrect bool   `json:"isCorrect"`
	Order     Order  `json:"order"`
}

type Attachment struct {
	Url         string `json:"url"`
	ContentType string `json:"contentType"`
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
		s.Data = res
	case MultiChoice:
		res := &MultiChoiceQuestion{}
		err := json.Unmarshal(questionData, res)
		if err != nil {
			//return nil
		}
		s.Data = res
	case FillInBlank:
		res := &FillInBlankQuestion{}
		err := json.Unmarshal(questionData, res)
		if err != nil {
			//return nil
		}
		s.Data = res
	case BlankWithMultiChoice:
		res := &BlankMultiChoiceQuestion{}
		err := json.Unmarshal(questionData, res)
		if err != nil {
			//return nil
		}
		s.Data = res
	case DragAndDrop:
		res := &DragAndDropQuestion{}
		err := json.Unmarshal(questionData, res)
		if err != nil {
			//return nil
		}
		s.Data = res
	}
}
