package question

import (
	"lms-class/common/xerr"
)

type WritingQuestion struct {
	Data
}

type WritingQuestionSession struct {
	WritingQuestion
	Answers []Key `json:"answers"`
}

func (*WritingQuestion) Shuffle() {
	// Do nothing
}

//var _ MarsalaUnmarshalAction = new(WritingQuestion)

type WritingMarsalaUnmarshalAction struct {
}

func (*WritingMarsalaUnmarshalAction) GetQuestionData() *IQuestion {
	var obj IQuestion = new(WritingQuestion)
	return &obj
}

func (*WritingMarsalaUnmarshalAction) GetQuestionSessionData(questionData interface{}) (interface{}, error) {
	question, ok := questionData.(WritingQuestion)
	if ok {
		t := &WritingQuestionSession{
			WritingQuestion: question,
			Answers:         []Key{""},
		}
		return t, nil
	}
	return nil, xerr.NewErrCodeAndInformation(xerr.QuestionDataNotCorrect)
}
