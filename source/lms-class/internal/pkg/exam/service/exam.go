package service

import (
	"context"
	"fmt"
	"github.com/devfeel/mapper"
	"github.com/pkg/errors"
	"lms-class/common/xerr"
	"lms-class/ent"
	"lms-class/ent/exam"
	"lms-class/internal/pkg/exam/dto"
	"lms-class/internal/pkg/exam/query"
	questionService "lms-class/internal/pkg/question/service"
	"lms-class/internal/services"
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

func IsExamExisted(id int) (bool, error) {
	return query.IsExamExisted(id)
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
	defer services.Commit(tx)
	res, err := query.GetExamForUpdate(id, tx)
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

func doPublishExam(id int, tx *ent.Tx) (*int, error) {
	res, err := tx.Exam.Query().Where(exam.IDEQ(id)).Only(context.Background())
	if err != nil {
		return nil, err
	}
	if res.HavingDraft == false {
		return nil, errors.New("exam hasn't draft yet")
	}
	_, err = res.Update().
		SetHavingDraft(false).
		SetLastPublishedAt(time.Now()).
		SetUpdatedAt(time.Now()).Save(context.Background())
	if err != nil {
		if rbErr := tx.Rollback(); rbErr != nil {
			return nil, fmt.Errorf("tx err: %v, rb err: %v", err, rbErr)
		}
		return nil, err
	}
	return &id, nil
}

func PublishExam(id int) (*int, error) {
	return services.WithTx(context.Background(), utils.EntClient, func(tx *ent.Tx) (*int, error) {
		return doPublishExam(id, tx)
	})
}

func GetPublishedExam(id int) (*dto.ExamDto, error) {
	latest, err := query.GetPublishedExam(id)
	if err != nil {
		if _, ok := err.(*ent.NotFoundError); ok {
			return nil, xerr.NewErrCodeAndInformation(xerr.ResourceNotFound, "exam")
		}
		return nil, err
	}
	if latest.LastPublishedAt == nil {
		return nil, errors.New("exam hasn't published yet")
	}
	dto := &dto.ExamDto{}
	if dto, err = mapExamHistory(latest); err != nil {
		return nil, err
	}
	publishedAt := latest.LastPublishedAt
	questionsDto, _ := questionService.GetQuestionsByExamAndPublishedTime(id, *publishedAt)
	dto.Questions = questionsDto
	return dto, nil
}

func mapExamHistory(res *ent.ExamHistory) (*dto.ExamDto, error) {
	dto := &dto.ExamDto{}
	mapper.SetEnableFieldIgnoreTag(true)
	err := mapper.AutoMapper(res, dto)
	dto.Id = res.Ref
	if err != nil {
		log.Print("unexpected error while mapping")
		return nil, err
	}
	return dto, nil
}

func GetExam(id int) (*dto.ExamDto, error) {
	res, err := utils.EntClient.Exam.Query().Where(exam.IDEQ(id)).Only(context.Background())
	if err != nil {
		if _, ok := err.(*ent.NotFoundError); ok {
			return nil, xerr.NewErrCodeAndInformation(xerr.ResourceNotFound, "exam")
		}
		return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
	}
	dto := &dto.ExamDto{}
	mapper.SetEnableFieldIgnoreTag(true)
	if err = mapper.AutoMapper(res, dto); err != nil {
		return nil, err
	}
	if res.HavingDraft == false {
		publishedAt := res.LastPublishedAt
		questionsDto, _ := questionService.GetQuestionsByExamAndPublishedTime(id, *publishedAt)
		dto.Questions = questionsDto
	} else {
		questionsDto, err := questionService.GetQuestionsByExam(id)
		if err != nil {
			return nil, err
		}
		dto.Questions = questionsDto
	}
	return dto, nil
}
