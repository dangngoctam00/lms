package queries

import (
	"context"
	"lms-class/ent"
	"lms-class/ent/quiz"
	"lms-class/pkg/utils"
)

func GetQuizById(id int) (*ent.Quiz, error) {
	return utils.EntClient.Quiz.
		Query().
		Where(quiz.ID(id)).
		Only(context.Background())
}
