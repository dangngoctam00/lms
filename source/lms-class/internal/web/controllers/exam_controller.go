package controllers

import (
	"github.com/gofiber/fiber/v2"
	"lms-class/common/result"
	"lms-class/internal/services"
	"lms-class/internal/web/dto/exam"
)

func GetExamById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	exam, err := services.GetExam(id)
	return result.HttpResult(c, exam, err)
}

func GetPublishedExamById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	exam, err := services.GetPublishedExam(id)
	return result.HttpResult(c, exam, err)
}

func CreateExam(c *fiber.Ctx) error {
	exam := &exam.ExamDto{}
	if err := c.BodyParser(exam); err != nil {
		return result.ParamErrorResult(c, err)
	}
	id, err := services.CreateExam(exam)
	return result.HttpResult(c, id, err)
}

func UpdateExam(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	exam := &exam.ExamDto{}
	if err := c.BodyParser(exam); err != nil {
		return result.ParamErrorResult(c, err)
	}
	err = services.UpdateExam(id, exam)
	return result.HttpResult(c, id, err)
}

func PublishExam(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	_, err = services.PublishExam(id)
	return result.HttpResult(c, id, err)
}
