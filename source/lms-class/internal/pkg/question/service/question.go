package service

import (
	"context"
	"errors"
	"fmt"
	"github.com/devfeel/mapper"
	"lms-class/common/xerr"
	"lms-class/ent"
	entQuestion "lms-class/ent/question"
	"lms-class/internal/pkg/question/dto"
	"lms-class/internal/pkg/question/query"
	"lms-class/internal/services"
	"lms-class/pkg/utils"
	"time"
)

func CreateQuestion(question *dto.Question) (*int, error) {
	return services.WithTx(context.Background(), utils.EntClient, func(tx *ent.Tx) (*int, error) {
		entity := utils.EntClient.Question.
			Create().
			SetContext(question.Context).
			SetContextId(question.ContextId).
			SetPosition(question.Position).
			SetQuestionType(question.QuestionType).
			SetData(question.Data).
			SetUpdatedAt(time.Now())
		id, err := query.CreateQuestion(entity)
		if err != nil {
			return nil, fmt.Errorf("failed creating question: %w", err)
		}
		return id, nil
	})
}

func GetQuestionById(id int) (*ent.Question, error) {
	result, err := utils.EntClient.Question.
		Query().
		Where(entQuestion.IDEQ(id)).
		Only(context.Background())
	if err != nil {
		return nil, fmt.Errorf("failed querying user: %w", err)
	}
	return result, nil
}

func UpdateQuestion(id int, dto *dto.Question) (*int, error) {
	exists, err := query.ExistsById(id)
	if err != nil {
		return nil, err
	}
	if !exists {
		return nil, xerr.NewErrCodeAndInformation(xerr.ResourceNotFound, "question")
	}
	return services.WithTx(context.Background(), utils.EntClient, func(tx *ent.Tx) (*int, error) {
		nextVer := time.Now().UnixNano()
		_, err = tx.Question.UpdateOneID(id).Where(entQuestion.ID(id), entQuestion.Version(dto.Version)).
			SetVersion(nextVer).
			SetContext(dto.Context).
			SetContextId(dto.ContextId).
			SetPosition(dto.Position).
			SetQuestionType(dto.QuestionType).
			SetData(dto.Data).
			SetUpdatedAt(time.Now()).
			Save(context.Background())
		if err != nil {
			if _, ok := err.(*ent.NotFoundError); ok {
				return nil, xerr.NewErrCodeAndInformation(xerr.ResourceNotFound, "question")
			}
			return nil, err
		}
		return &id, nil
	})
}

func GetQuestionsByExamAndPublishedTime(examId int, lastPublished time.Time) ([]dto.QuestionDto, error) {
	all, err := query.GetQuestionsByExamAndPublishedTime(examId, lastPublished)
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
	dto := &dto.QuestionDto{}
	if err := dto.SetQuestionData(questionHistory.QuestionType, questionHistory.Data); err != nil {
		return nil, err
	}
	mapper.SetEnableFieldIgnoreTag(true)
	if err := mapper.AutoMapper(questionHistory, dto); err != nil {
		return nil, errors.New("unexpected error while mapping")
	}
	dto.ID = questionHistory.Ref
	dto.UpdatedAt = &questionHistory.UpdatedAt
	return dto, nil
}

func GetQuestionsByExam(examId int) ([]dto.QuestionDto, error) {
	all, err := query.GetQuestionsByExam(examId)
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
	dto := &dto.QuestionDto{}
	if err := dto.SetQuestionData(v.QuestionType, v.Data); err != nil {
		return nil, err
	}
	mapper.SetEnableFieldIgnoreTag(true)
	if err := mapper.AutoMapper(v, dto); err != nil {
		return nil, errors.New("unexpected error while mapping")
	}
	return dto, nil
}
