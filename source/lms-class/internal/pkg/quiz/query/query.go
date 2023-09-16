package query

import (
	"context"
	"entgo.io/ent/dialect/sql"
	"lms-class/ent"
	"lms-class/ent/quiz"
	"lms-class/ent/quizsubmission"
	"lms-class/pkg"
	"lms-class/pkg/utils"
)

func GetQuizSubmissionById(id int) (*ent.QuizSubmission, error) {
	return utils.EntClient.QuizSubmission.
		Query().
		Where(quizsubmission.ID(id)).
		Only(context.Background())
}

func GetQuizSubmissionByIdOnUpdate(tx *ent.Tx, id int) (*ent.QuizSubmission, error) {
	return tx.QuizSubmission.Query().
		WithQuiz().
		Where(quizsubmission.ID(id)).
		ForUpdate().
		Only(context.Background())
}

func GetUnSubmittedQuizSessions() ([]*ent.QuizSubmission, error) {
	return utils.EntClient.QuizSubmission.Query().
		WithQuiz().
		Where(quizsubmission.SubmittedAtIsNil()).
		Where(func(selector *sql.Selector) {
			qT := sql.Table(quiz.Table).Schema(pkg.GetTenant())
			qST := sql.Table(quizsubmission.Table).Schema(pkg.GetTenant())
			selector.Join(qT).On(qST.C(quizsubmission.FieldQuizId), qT.C(quiz.FieldID))
			selector.Where(sql.NotNull(qT.C(quiz.FieldTimeLimit)))
		}).
		All(context.Background())
}
