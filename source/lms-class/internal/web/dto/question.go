package dto

import (
	"encoding/json"
	"math/rand"
	"time"
)

type Order int
type Key string

type AnswerQuestion struct {
	Answers []Key `json:"answers"`
}

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

type WritingQuestionSession struct {
	WritingQuestion
	Answers []Key `json:"answers"`
}

func (*WritingQuestion) Shuffle() {
	// Do nothing
}

type MultiChoiceQuestion struct {
	QuestionData
	IsMultipleAnswer bool                `json:"isMultipleAnswer"`
	Options          []MultiChoiceOption `json:"options"`
}

type MultiChoiceQuestionSession struct {
	MultiChoiceQuestion
	Answers []Key `json:"answers"`
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

type BlankMultiChoiceQuestionSession struct {
	BlankMultiChoiceQuestion
	Answers []Key `json:"answers"`
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

type FillInBlankQuestionSession struct {
	FillInBlankQuestion
	Answers []Key `json:"answers"`
}

func (*FillInBlankQuestion) Shuffle() {
	// Do nothing
}

type DragAndDropQuestion struct {
	QuestionData
	Blanks     []BlankDragAndDrop  `json:"blanks"`
	Candidates []AnswerDragAndDrop `json:"candidates"`
}

type DragAndDropQuestionSession struct {
	DragAndDropQuestion
	Answers []Key `json:"answers"`
}

func (*DragAndDropQuestion) Shuffle() {
	// Do nothing
}

type BlankDragAndDrop struct {
	AnswerKey Key   `json:"answerKey"`
	Order     Order `json:"order"`
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
	Key       Key    `json:"key"`
	Content   string `json:"content"`
	IsCorrect bool   `json:"isCorrect"`
	Order     Order  `json:"order"`
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

type BlankDragAndDropAnswer struct {
	Answer
	Answers []string `json:"answers"`
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

func (s *QuestionDto) SetData(questionType string, data json.RawMessage) {
	s.DoSetData(questionType, data)
}

func (s *QuestionQuizSession) SetData(questionType string, questionData interface{}) {
	switch questionType {
	case Writing:
		question, ok := questionData.(WritingQuestion)
		if ok {
			t := &WritingQuestionSession{
				WritingQuestion: question,
				Answers:         []Key{""},
			}
			s.Data = t
		}
	case MultiChoice:
		question, ok := questionData.(MultiChoiceQuestion)
		if ok {
			t := &MultiChoiceQuestionSession{
				MultiChoiceQuestion: question,
				Answers:             []Key{""},
			}
			s.Data = t
		}
	case FillInBlank:
		question, ok := questionData.(FillInBlankQuestion)
		if ok {
			t := &FillInBlankQuestionSession{
				FillInBlankQuestion: question,
				Answers:             []Key{""},
			}
			s.Data = t
		}
	case BlankWithMultiChoice:
		question, ok := questionData.(BlankMultiChoiceQuestion)
		if ok {
			t := &BlankMultiChoiceQuestionSession{
				BlankMultiChoiceQuestion: question,
				Answers:                  []Key{""},
			}
			s.Data = t
		}
	case DragAndDrop:
		question, ok := questionData.(DragAndDropQuestion)
		if ok {
			t := &DragAndDropQuestionSession{
				DragAndDropQuestion: question,
				Answers:             []Key{""},
			}
			s.Data = t
		}
	}
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
