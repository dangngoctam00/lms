package dto

import (
	"lms-class/common/xerr"
)

type DragAndDropQuestion struct {
	Data
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

type BlankDragAndDropAnswer struct {
	Answer
	Answers []string `json:"answers"`
}

type DragAndDropMarsalaUnmarshalAction struct {
}

func (*DragAndDropMarsalaUnmarshalAction) GetQuestionData() *IQuestion {
	var obj IQuestion = new(DragAndDropQuestion)
	return &obj
}

func (*DragAndDropMarsalaUnmarshalAction) GetQuestionSessionData(questionData interface{}) (interface{}, error) {
	question, ok := questionData.(DragAndDropQuestion)
	if ok {
		t := &DragAndDropQuestionSession{
			DragAndDropQuestion: question,
			Answers:             []Key{""},
		}
		return t, nil
	}
	return nil, xerr.NewErrCodeAndInformation(xerr.QuestionDataNotCorrect)
}
