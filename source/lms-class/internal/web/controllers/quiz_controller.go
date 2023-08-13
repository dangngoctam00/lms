package controllers

import (
	"github.com/gofiber/fiber/v2"
	"lms-class/common/result"
	"lms-class/internal/services"
	"lms-class/internal/web/dto"
)

func CreateQuiz(c *fiber.Ctx) error {
	quiz := &dto.QuizDto{}
	if err := c.BodyParser(quiz); err != nil {
		return result.ParamErrorResult(c, err)
	}
	id, err := services.CreateQuiz(quiz)
	response := &dto.QuizDto{}
	response.WithId(id)
	return result.HttpResult(c, response, err)
}

func GetQuizById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	byId, err := services.GetQuizById(id)
	return result.HttpResult(c, byId, err)
}

func GetQuizSessionById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	byId, err := services.GetQuizSession(id)
	return result.HttpResult(c, byId, err)
}

func DoQuiz(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	quiz, err := services.DoQuiz(id)
	return result.HttpResult(c, quiz, err)
}
