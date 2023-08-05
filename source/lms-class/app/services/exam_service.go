package services

import (
	"context"
	"errors"
	"fmt"
	"github.com/devfeel/mapper"
	"lms-class/app/queries"
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
	tx, err2 := utils.EntClient.Tx(context.Background())
	if err2 != nil {
		log.Fatalf("failed creating transaction: %v", err2)
	}
	defer Commit(tx)
	res, err := queries.GetExamForUpdate(id, tx)
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
		if err2 := tx.Rollback(); err2 != nil {
			log.Fatalln("Error while trying roll back exception: ", err2)
		}
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
	latest, err := queries.GetPublishedExam(id)
	if err != nil {
		return nil, err
	}
	if latest.LastPublishedAt == nil {
		return nil, errors.New("exam hasn't published yet")
	}
	examDto := &dto.ExamDto{}
	if examDto, err = mapExamHistory(latest); err != nil {
		return nil, err
	}
	publishedAt := latest.LastPublishedAt
	questionsDto, _ := GetQuestionsByExamAndPublishedTime(id, *publishedAt)
	examDto.Questions = questionsDto
	return examDto, nil
}

func mapExam(res *ent.Exam) (*dto.ExamDto, error) {
	examDto := &dto.ExamDto{}
	mapper.SetEnableFieldIgnoreTag(true)
	err := mapper.AutoMapper(res, examDto)
	if err != nil {
		log.Print("unexpected error while mapping")
		return nil, err
	}
	return examDto, nil
}

func mapExamHistory(res *ent.ExamHistory) (*dto.ExamDto, error) {
	examDto := &dto.ExamDto{}
	mapper.SetEnableFieldIgnoreTag(true)
	err := mapper.AutoMapper(res, examDto)
	examDto.Id = res.Ref
	if err != nil {
		log.Print("unexpected error while mapping")
		return nil, err
	}
	return examDto, nil
}

func GetExam(id int) (*dto.ExamDto, error) {
	res, err := utils.EntClient.Exam.Query().Where(exam.IDEQ(id)).Only(context.Background())
	if err != nil {
		return nil, err
	}
	examDto := &dto.ExamDto{}
	mapper.SetEnableFieldIgnoreTag(true)
	if err = mapper.AutoMapper(res, examDto); err != nil {
		return nil, err
	}
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
