package query

import (
	"context"
	"entgo.io/ent/dialect/sql"
	"lms-class/ent"
	"lms-class/ent/exam"
	"lms-class/ent/examhistory"
	"lms-class/pkg"
	"lms-class/pkg/utils"
)

func GetPublishedExam(id int) (*ent.ExamHistory, error) {
	latest, err := utils.EntClient.ExamHistory.Query().
		Where(func(s *sql.Selector) {
			t := sql.Table(examhistory.Table)
			t.Schema(pkg.GetTenant())
			s.From(t).Select().Where(sql.EQ(t.C(examhistory.FieldRef), id)).
				Where(sql.IsFalse(t.C(examhistory.FieldHavingDraft)))
		}).Latest(context.Background())
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
