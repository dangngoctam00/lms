package dto

import (
	"lms-class/common/xerr"
)

type FillInBlankQuestion struct {
	Data
	Blanks []Blank `json:"blanks"`
}

type Blank struct {
	ExpectedAnswer string `json:"expectedAnswer"`
	Strategy       string `json:"strategy"`
	Order          Order  `json:"order"`
}

type FillInBlankQuestionSession struct {
	FillInBlankQuestion
	Answers []Key `json:"answers"`
}

func (*FillInBlankQuestion) Shuffle() {
	// Do nothing
}

type FillInBlankMarsalaUnmarshalAction struct {
}

func (*FillInBlankMarsalaUnmarshalAction) GetQuestionData() *IQuestion {
	var obj IQuestion = new(FillInBlankQuestion)
	return &obj
}

func (*FillInBlankMarsalaUnmarshalAction) GetQuestionSessionData(questionData interface{}) (interface{}, error) {
	question, ok := questionData.(FillInBlankQuestion)
	if ok {
		t := &FillInBlankQuestionSession{
			FillInBlankQuestion: question,
			Answers:             []Key{""},
		}
		return t, nil
	}
	return nil, xerr.NewErrCodeAndInformation(xerr.QuestionDataNotCorrect)
}
