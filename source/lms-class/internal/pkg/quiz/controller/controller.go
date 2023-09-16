package controller

import (
	"github.com/gofiber/fiber/v2"
	"lms-class/common/result"
	"lms-class/internal/pkg/question/dto"
	dto2 "lms-class/internal/pkg/quiz/dto"
	"lms-class/internal/pkg/quiz/service"
)

func CreateQuiz(c *fiber.Ctx) error {
	dto := &dto2.QuizDto{}
	if err := c.BodyParser(dto); err != nil {
		return result.ParamErrorResult(c, err)
	}
	id, err := service.CreateQuiz(dto)
	response := &dto2.QuizDto{}
	response.WithId(id)
	return result.HttpResult(c, response, err)
}

func GetQuizById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	byId, err := service.GetQuizById(id)
	return result.HttpResult(c, byId, err)
}

func GetQuizSessionById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	byId, err := service.GetQuizSession(id)
	return result.HttpResult(c, byId, err)
}

func AnswerQuestionById(c *fiber.Ctx) error {
	sessionId, err := c.ParamsInt("sessionId")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	questionId, err := c.ParamsInt("questionId")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	answers := &dto.AnswerQuestion{}
	if err := c.BodyParser(answers); err != nil {
		return result.ParamErrorResult(c, err)
	}
	id, err := service.AnswerQuestionById(sessionId, questionId, answers.Answers)
	return result.HttpResult(c, id, err)
}

func DoQuiz(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	quizEntity, err := service.DoQuiz(id)
	return result.HttpResult(c, quizEntity, err)
}
