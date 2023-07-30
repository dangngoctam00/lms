package services

import (
	"context"
	"fmt"
	"github.com/devfeel/mapper"
	"lms-class/app/web/dto"
	"lms-class/ent"
	"lms-class/ent/question"
	"lms-class/ent/questionhistory"
	"lms-class/pkg/utils"
	"log"
	"time"
)

func CreateQuestion(question *dto.Question) (*ent.Question, error) {
	//if question.Context == dto.Exam {
	//
	//}
	u, err := utils.EntClient.Question.
		Create().
		SetContext(question.Context).
		SetContextId(question.ContextId).
		SetPosition(question.Position).
		SetQuestionType(question.QuestionType).
		SetData(question.Data).
		SetUpdatedAt(time.Now()).
		Save(context.Background())
	if err != nil {
		return nil, fmt.Errorf("failed creating exam: %w", err)
	}
	log.Println("Question was created: ", u)
	return u, nil
}

func GetQuestionById(id int) (*ent.Question, error) {
	result, err := utils.EntClient.Question.
		Query().
		Where(question.IDEQ(id)).
		Only(context.Background())
	if err != nil {
		return nil, fmt.Errorf("failed querying user: %w", err)
	}
	return result, nil
}

func GetQuestionsByExamAndPublishedTime(examId int, lastPublished time.Time) ([]dto.QuestionDto, error) {
	all, err := utils.EntClient.QuestionHistory.
		Query().
		Where(questionhistory.ContextEQ(dto.Exam),
			questionhistory.ContextIdEQ(examId),
			questionhistory.HistoryTimeLTE(lastPublished)).
		All(context.Background())
	if err != nil {
		return nil, err
	}
	res := make([]dto.QuestionDto, len(all))
	for i, v := range all {
		questionDto := dto.QuestionDto{}
		questionDto.DoSetData(v.QuestionType, v.Data)

		mapper.SetEnableFieldIgnoreTag(true)
		mapper.AutoMapper(v, &questionDto)
		res[i] = questionDto
	}
	return res, nil
}

func GetQuestionsByExam(examId int) ([]dto.QuestionDto, error) {
	all, err := utils.EntClient.Question.
		Query().
		Where(question.ContextEQ(dto.Exam),
			question.ContextIdEQ(examId)).
		All(context.Background())
	if err != nil {
		return nil, err
	}
	res := make([]dto.QuestionDto, len(all))
	for i, v := range all {
		questionDto := dto.QuestionDto{}
		questionDto.SetData(*v)

		mapper.SetEnableFieldIgnoreTag(true)
		mapper.AutoMapper(v, &questionDto)
		res[i] = questionDto
	}
	return res, nil
}
