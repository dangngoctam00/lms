package services

import (
	"context"
	"errors"
	"fmt"
	"github.com/devfeel/mapper"
	"lms-class/app/web/dto"
	"lms-class/ent"
	"lms-class/ent/exam"
	"lms-class/pkg/utils"
	"log"
	"time"
)

type AlreadyHavingDraft struct {
}

func init() {
	if err := mapper.Register(&dto.ExamDto{}); err != nil {
		log.Fatal("error while registering mapper exam")
		return
	}
	if err := mapper.Register(&ent.Exam{}); err != nil {
		log.Fatal("error while registering mapper exam")
		return
	}
}

func CreateExam(exam *dto.ExamDto) (*ent.Exam, error) {
	u, err := utils.EntClient.Exam.
		Create().
		SetTitle(exam.Title).
		SetContext(exam.Context).
		SetContextId(exam.ContextId).
		SetIsPublished(exam.IsPublished).
		SetHavingDraft(true).
		SetUpdatedAt(time.Now()).
		Save(context.Background())
	if err != nil {
		return nil, fmt.Errorf("failed creating exam: %w", err)
	}
	log.Println("Exam was created: ", u)
	return u, nil
}

func UpdateExam(id int, examDto *dto.ExamDto) error {
	//tx, _ := utils.EntClient.Tx(context.Background())
	//tx.Exam.Query().Where(exam.ID(id)).For
	tx, err2 := utils.EntClient.Tx(context.Background())
	if err2 != nil {
		log.Fatalf("failed creating transaction: %v", err2)
	}
	defer func(tx *ent.Tx) {
		if err := tx.Commit(); err != nil {
			tx.Rollback()
			log.Fatal("unexpected failure:", err)
		}
	}(tx)
	res, err := tx.Exam.Query().
		Where(exam.IDEQ(id)).
		ForUpdate().
		Only(context.Background())
	if err != nil {
		return err
	}
	if res.HavingDraft == true {
		return errors.New("exam has draft already")
	}
	_, err = res.Update().
		SetTitle(examDto.Title).
		SetContext(examDto.Context).
		SetContextId(examDto.ContextId).
		SetIsPublished(examDto.IsPublished).
		SetHavingDraft(true).
		SetUpdatedAt(time.Now()).
		Save(context.Background())
	if err != nil {
		return err
	}
	return nil
}

func PublishExam(id int) error {
	res, err := utils.EntClient.Exam.Query().Where(exam.IDEQ(id)).Only(context.Background())
	if err != nil {
		return err
	}
	if res.HavingDraft == false {
		return errors.New("exam hasn't draft yet")
	}
	_, err = res.Update().
		SetHavingDraft(false).
		SetLastPublishedAt(time.Now()).
		SetUpdatedAt(time.Now()).Save(context.Background())
	if err != nil {
		return errors.New("unknown error")
	}
	return nil
}

func GetPublishedExam(id int) (*dto.ExamDto, error) {
	res, err := utils.EntClient.Exam.Query().Where(exam.IDEQ(id)).Only(context.Background())
	if err != nil {
		return nil, err
	}
	if res.LastPublishedAt == nil {
		return nil, errors.New("exam hasn't published yet")
	}
	examDto := &dto.ExamDto{}
	mapper.SetEnableFieldIgnoreTag(true)
	mapper.AutoMapper(res, examDto)
	publishedAt := res.LastPublishedAt
	questionsDto, _ := GetQuestionsByExamAndPublishedTime(id, *publishedAt)
	examDto.Questions = questionsDto
	return examDto, nil
}

func GetExam(id int) (*dto.ExamDto, error) {
	res, err := utils.EntClient.Exam.Query().Where(exam.IDEQ(id)).Only(context.Background())
	if err != nil {
		return nil, err
	}
	examDto := &dto.ExamDto{}
	mapper.SetEnableFieldIgnoreTag(true)
	mapper.AutoMapper(res, examDto)
	if res.HavingDraft == false {
		publishedAt := res.LastPublishedAt
		questionsDto, _ := GetQuestionsByExamAndPublishedTime(id, *publishedAt)
		examDto.Questions = questionsDto
	} else {
		questionsDto, _ := GetQuestionsByExam(id)
		examDto.Questions = questionsDto
	}
	return examDto, nil
}
