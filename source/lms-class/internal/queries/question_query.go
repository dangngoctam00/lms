package queries

import (
	"context"
	"entgo.io/ent/dialect/sql"
	"github.com/flume/enthistory"
	"lms-class/ent"
	"lms-class/ent/question"
	"lms-class/ent/questionhistory"
	"lms-class/internal/web/dto"
	"lms-class/pkg/utils"
	"time"
)

func CreateQuestion(question *ent.QuestionCreate) (*int, error) {
	saved, err := question.Save(context.Background())
	if err != nil {
		return nil, err
	}
	return &saved.ID, nil
}

func GetQuestionsByExamAndPublishedTime(examId int, lastPublished time.Time) ([]*ent.QuestionHistory, error) {
	all, err := utils.EntClient.QuestionHistory.Query().
		Where(func(s *sql.Selector) {
			t := sql.Table(questionhistory.Table).As("q2")
			subQuery := sql.Select(sql.Max(t.C(questionhistory.FieldHistoryTime))).
				From(t).Where(sql.EQ(t.C(questionhistory.FieldRef), sql.Expr(s.C(questionhistory.FieldRef))))
			subQuery.Where(sql.EQ(t.C(questionhistory.FieldContextId), examId))
			subQuery.Where(sql.EQ(t.C(questionhistory.FieldContext), "EXAM"))
			subQuery.Where(sql.LTE(t.C(questionhistory.FieldHistoryTime), lastPublished))
			s.Where(sql.EQ(s.C(questionhistory.FieldHistoryTime), subQuery))
			s.Where(sql.NEQ(s.C(questionhistory.FieldOperation), enthistory.OpTypeDelete))
		}).All(context.Background())
	return all, err
}

func GetQuestionsByExam(examId int) ([]*ent.Question, error) {
	return utils.EntClient.Question.
		Query().
		Where(question.ContextEQ(dto.Exam),
			question.ContextIdEQ(examId)).
		All(context.Background())
}
