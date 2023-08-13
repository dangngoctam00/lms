package services

import (
	"context"
	"errors"
	"fmt"
	"github.com/devfeel/mapper"
	"lms-class/ent"
	"lms-class/ent/question"
	"lms-class/internal/queries"
	"lms-class/internal/web/dto"
	"lms-class/pkg/utils"
	"time"
)

func CreateQuestion(question *dto.Question) (*int, error) {
	entity := utils.EntClient.Question.
		Create().
		SetContext(question.Context).
		SetContextId(question.ContextId).
		SetPosition(question.Position).
		SetQuestionType(question.QuestionType).
		SetData(question.Data).
		SetUpdatedAt(time.Now())
	id, err := queries.CreateQuestion(entity)
	if err != nil {
		return nil, fmt.Errorf("failed creating question: %w", err)
	}
	return id, nil
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

func UpdateQuestion(id int, dto *dto.Question) error {
	tx, err := utils.EntClient.Tx(context.Background())
	if err != nil {
		return errors.New("cannot create transaction because of " + err.Error())
	}
	_, err = GetQuestionById(id)
	if err != nil {
		return err
	}
	nextVer := time.Now().UnixNano()
	_, err = tx.Question.UpdateOneID(id).Where(question.ID(id), question.Version(dto.Version)).
		SetVersion(nextVer).
		SetContext(dto.Context).
		SetContextId(dto.ContextId).
		SetPosition(dto.Position).
		SetQuestionType(dto.QuestionType).
		SetData(dto.Data).
		SetUpdatedAt(time.Now()).
		Save(context.Background())
	if err != nil {
		return err
	}
	return nil
}

func GetQuestionsByExamAndPublishedTime(examId int, lastPublished time.Time) ([]dto.QuestionDto, error) {
	all, err := queries.GetQuestionsByExamAndPublishedTime(examId, lastPublished)
	if err != nil {
		return nil, err
	}
	res := make([]dto.QuestionDto, len(all))
	for i, v := range all {
		if d, err := mapQuestionHistory(v); err != nil {
			return nil, err
		} else {
			res[i] = *d
		}
	}
	return res, nil
}

func mapQuestionHistory(questionHistory *ent.QuestionHistory) (*dto.QuestionDto, error) {
	questionDto := &dto.QuestionDto{}
	questionDto.DoSetData(questionHistory.QuestionType, questionHistory.Data)
	mapper.SetEnableFieldIgnoreTag(true)
	if err := mapper.AutoMapper(questionHistory, questionDto); err != nil {
		return nil, errors.New("unexpected error while mapping")
	}
	questionDto.ID = questionHistory.Ref
	questionDto.UpdatedAt = &questionHistory.UpdatedAt
	return questionDto, nil
}

func GetQuestionsByExam(examId int) ([]dto.QuestionDto, error) {
	all, err := queries.GetQuestionsByExam(examId)
	if err != nil {
		return nil, err
	}
	res := make([]dto.QuestionDto, len(all))
	for i, v := range all {
		if d, err := mapQuestion(v); err != nil {
			return nil, err
		} else {
			res[i] = *d
		}
	}
	return res, nil
}

func mapQuestion(v *ent.Question) (*dto.QuestionDto, error) {
	questionDto := &dto.QuestionDto{}
	questionDto.SetData(*v)
	mapper.SetEnableFieldIgnoreTag(true)
	if err := mapper.AutoMapper(v, questionDto); err != nil {
		return nil, errors.New("unexpected error while mapping")
	}
	return questionDto, nil
}
