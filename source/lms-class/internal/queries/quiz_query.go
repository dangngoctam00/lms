package queries

import (
	"context"
	"lms-class/ent"
	"lms-class/ent/quiz"
	"lms-class/ent/quizsubmission"
	"lms-class/pkg/utils"
)

func GetQuizById(id int) (*ent.Quiz, error) {
	return utils.EntClient.Quiz.
		Query().
		Where(quiz.ID(id)).
		Only(context.Background())
}

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
