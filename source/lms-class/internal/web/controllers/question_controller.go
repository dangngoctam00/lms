package controllers

import (
	"github.com/devfeel/mapper"
	"github.com/gofiber/fiber/v2"
	"lms-class/common/result"
	"lms-class/ent"
	"lms-class/internal/services"
	questionDto "lms-class/internal/web/dto/question"
	"log"
)

func init() {
	if err := mapper.Register(&questionDto.QuestionDto{}); err != nil {
		log.Fatal("error while registering mapper exam")
		return
	}
	if err := mapper.Register(&ent.Question{}); err != nil {
		log.Fatal("error while registering mapper exam")
		return
	}
}

func CreateQuestion(c *fiber.Ctx) error {
	question := &questionDto.Question{}
	if err := c.BodyParser(question); err != nil {
		return result.ParamErrorResult(c, err)
	}
	id, err := services.CreateQuestion(question)
	return result.HttpResult(c, id, err)
}

func UpdateQuestion(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	question := &questionDto.Question{}
	if err := c.BodyParser(question); err != nil {
		return result.ParamErrorResult(c, err)
	}
	return result.HttpResult(c, id, err)
}

func GetQuestionById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	question, err := services.GetQuestionById(id)
	if err != nil {
		return result.HttpResult(c, nil, err)
	}
	dto := questionDto.QuestionDto{}
	if err := dto.SetQuestionData(question.QuestionType, question.Data); err != nil {
		return err
	}

	mapper.SetEnableFieldIgnoreTag(true)
	if err = mapper.AutoMapper(question, &dto); err != nil {
		return result.HttpResult(c, nil, err)
	}
	return result.HttpResult(c, dto, err)
}
