package controller

import (
	"github.com/gofiber/fiber/v2"
	"lms-class/common/result"
	"lms-class/internal/pkg/exam/dto"
	"lms-class/internal/pkg/exam/service"
)

func GetExamById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	exam, err := service.GetExam(id)
	return result.HttpResult(c, exam, err)
}

func GetPublishedExamById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	exam, err := service.GetPublishedExam(id)
	return result.HttpResult(c, exam, err)
}

func CreateExam(c *fiber.Ctx) error {
	exam := &dto.ExamDto{}
	if err := c.BodyParser(exam); err != nil {
		return result.ParamErrorResult(c, err)
	}
	id, err := service.CreateExam(exam)
	return result.HttpResult(c, id, err)
}

func UpdateExam(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	exam := &dto.ExamDto{}
	if err := c.BodyParser(exam); err != nil {
		return result.ParamErrorResult(c, err)
	}
	err = service.UpdateExam(id, exam)
	return result.HttpResult(c, id, err)
}

func PublishExam(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	_, err = service.PublishExam(id)
	return result.HttpResult(c, id, err)
}
