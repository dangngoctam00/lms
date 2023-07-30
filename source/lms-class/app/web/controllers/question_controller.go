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

func CreateQuestion(c *fiber.Ctx) error {
	question := &dto.Question{}
	if err := c.BodyParser(question); err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{
			Code:    -1,
			Message: err.Error(),
		})
	}
	result, err := services.CreateQuestion(question)
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	return c.Status(fiber.StatusOK).JSON(dto.LmsResponseApi[ent.Question]{Code: 0, Message: "OK", Value: *result})
}

func GetQuestionById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	result, err := services.GetQuestionById(id)
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(dto.LmsErrorResponseApi{Code: -1, Message: err.Error()})
	}
	questionDto := dto.QuestionDto{}
	questionDto.SetData(*result)

	mapper.SetEnableFieldIgnoreTag(true)
	mapper.AutoMapper(result, &questionDto)
	return c.Status(fiber.StatusOK).JSON(dto.LmsResponseApi[dto.QuestionDto]{Code: 0, Message: "OK", Value: questionDto})
	//return c.Status(fiber.StatusOK).JSON(dto.LmsResponseApi[ent.Question]{Code: 0, Message: "OK", Value: *result})
}
