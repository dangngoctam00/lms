package question

import (
	"lms-class/common/xerr"
	"math/rand"
	"time"
)

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

type BlankMultiChoiceQuestion struct {
	Data
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

type BlankMultiChoiceMarsalaUnmarshalAction struct {
}

func (*BlankMultiChoiceMarsalaUnmarshalAction) GetQuestionData() *IQuestion {
	var obj IQuestion = new(BlankMultiChoiceQuestion)
	return &obj
}

func (*BlankMultiChoiceMarsalaUnmarshalAction) GetQuestionSessionData(questionData interface{}) (interface{}, error) {
	question, ok := questionData.(BlankMultiChoiceQuestion)
	if ok {
		t := &BlankMultiChoiceQuestionSession{
			BlankMultiChoiceQuestion: question,
			Answers:                  []Key{""},
		}
		return t, nil
	}
	return nil, xerr.NewErrCodeAndInformation(xerr.QuestionDataNotCorrect)
}
