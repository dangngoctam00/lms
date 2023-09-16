package dto

import (
	"lms-class/common/xerr"
	"math/rand"
	"time"
)

type MultiChoiceQuestion struct {
	Data
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

type MultiChoiceMarsalaUnmarshalAction struct {
}

func (*MultiChoiceMarsalaUnmarshalAction) GetQuestionData() *IQuestion {
	var obj IQuestion = new(MultiChoiceQuestion)
	return &obj
}

func (*MultiChoiceMarsalaUnmarshalAction) GetQuestionSessionData(questionData interface{}) (interface{}, error) {
	question, ok := questionData.(MultiChoiceQuestion)
	if ok {
		t := &MultiChoiceQuestionSession{
			MultiChoiceQuestion: question,
			Answers:             []Key{""},
		}
		return t, nil
	}
	return nil, xerr.NewErrCodeAndInformation(xerr.QuestionDataNotCorrect)
}
