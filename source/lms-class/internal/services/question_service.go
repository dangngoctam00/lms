package services

import (
	"context"
	"errors"
	"fmt"
	"github.com/devfeel/mapper"
	"lms-class/ent"
	entQuestion "lms-class/ent/question"
	"lms-class/internal/queries"
	questionDto "lms-class/internal/web/dto/question"
	"lms-class/pkg/utils"
	"time"
)

func CreateQuestion(question *questionDto.Question) (*int, error) {
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
		Where(entQuestion.IDEQ(id)).
		Only(context.Background())
	if err != nil {
		return nil, fmt.Errorf("failed querying user: %w", err)
	}
	return result, nil
}

func UpdateQuestion(id int, dto *questionDto.Question) error {
	tx, err := utils.EntClient.Tx(context.Background())
	if err != nil {
		return errors.New("cannot create transaction because of " + err.Error())
	}
	_, err = GetQuestionById(id)
	if err != nil {
		return err
	}
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
		return err
	}
	return nil
}

func GetQuestionsByExamAndPublishedTime(examId int, lastPublished time.Time) ([]questionDto.QuestionDto, error) {
	all, err := queries.GetQuestionsByExamAndPublishedTime(examId, lastPublished)
	if err != nil {
		return nil, err
	}
	res := make([]questionDto.QuestionDto, len(all))
	for i, v := range all {
		if d, err := mapQuestionHistory(v); err != nil {
			return nil, err
		} else {
			res[i] = *d
		}
	}
	return res, nil
}

func mapQuestionHistory(questionHistory *ent.QuestionHistory) (*questionDto.QuestionDto, error) {
	dto := &questionDto.QuestionDto{}
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

func GetQuestionsByExam(examId int) ([]questionDto.QuestionDto, error) {
	all, err := queries.GetQuestionsByExam(examId)
	if err != nil {
		return nil, err
	}
	res := make([]questionDto.QuestionDto, len(all))
	for i, v := range all {
		if d, err := mapQuestion(v); err != nil {
			return nil, err
		} else {
			res[i] = *d
		}
	}
	return res, nil
}

func mapQuestion(v *ent.Question) (*questionDto.QuestionDto, error) {
	dto := &questionDto.QuestionDto{}
	if err := dto.SetQuestionData(v.QuestionType, v.Data); err != nil {
		return nil, err
	}
	mapper.SetEnableFieldIgnoreTag(true)
	if err := mapper.AutoMapper(v, dto); err != nil {
		return nil, errors.New("unexpected error while mapping")
	}
	return dto, nil
}
