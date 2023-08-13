package queries

import (
	"context"
	"lms-class/ent"
	"lms-class/ent/exam"
	"lms-class/ent/examhistory"
	"lms-class/pkg/utils"
)

func GetPublishedExam(id int) (*ent.ExamHistory, error) {
	latest, err := utils.EntClient.ExamHistory.Query().Where(examhistory.Ref(id), examhistory.HavingDraft(false)).Latest(context.Background())
	return latest, err
}

func IsExamExisted(id int) (bool, error) {
	return utils.EntClient.Exam.Query().Where(exam.ID(id)).Exist(context.Background())
}

func GetExamForUpdate(id int, tx *ent.Tx) (*ent.Exam, error) {
	res, err := tx.Exam.Query().
		Where(exam.IDEQ(id)).
		ForUpdate().
		Only(context.Background())
	return res, err
}
