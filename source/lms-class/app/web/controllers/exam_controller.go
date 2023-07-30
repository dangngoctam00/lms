package controllers

import (
	"github.com/devfeel/mapper"
	"github.com/gofiber/fiber/v2"
	"lms-class/app/services"
	"lms-class/app/web/dto"
	"lms-class/ent"
)

func init() {
	mapper.Register(&dto.QuestionDto{})
	mapper.Register(&ent.Question{})
}

func GetExamById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	result, err := services.GetExam(id)
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	return c.Status(fiber.StatusOK).JSON(dto.LmsResponseApi[dto.ExamDto]{Code: 0, Message: "OK", Value: *result})
}

func GetPublishedExamById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	result, err := services.GetPublishedExam(id)
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	return c.Status(fiber.StatusOK).JSON(dto.LmsResponseApi[dto.ExamDto]{Code: 0, Message: "OK", Value: *result})
}

func CreateExam(c *fiber.Ctx) error {
	exam := &dto.ExamDto{}
	if err := c.BodyParser(exam); err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{
			Code:    -1,
			Message: err.Error(),
		})
	}
	result, err := services.CreateExam(exam)
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	return c.Status(fiber.StatusOK).JSON(dto.LmsResponseApi[ent.Exam]{Code: 0, Message: "OK", Value: *result})
}

func UpdateExam(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	exam := &dto.ExamDto{}
	if err := c.BodyParser(exam); err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{
			Code:    -1,
			Message: err.Error(),
		})
	}
	err = services.UpdateExam(id, exam)
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	return c.Status(fiber.StatusOK).JSON(dto.LmsResponseApi[ent.Exam]{Code: 0, Message: "OK"})
}

func PublishExam(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	err = services.PublishExam(id)
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	return c.Status(fiber.StatusOK).JSON(dto.LmsResponseApi[ent.Exam]{Code: 0, Message: "OK"})
}
