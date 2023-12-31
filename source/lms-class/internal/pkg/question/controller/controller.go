package controller

import (
	"github.com/devfeel/mapper"
	"github.com/gofiber/fiber/v2"
	"lms-class/common/result"
	"lms-class/ent"
	"lms-class/internal/pkg/question/dto"
	"lms-class/internal/pkg/question/service"
	"log"
)

func init() {
	if err := mapper.Register(&dto.QuestionDto{}); err != nil {
		log.Fatal("error while registering mapper exam")
		return
	}
	if err := mapper.Register(&ent.Question{}); err != nil {
		log.Fatal("error while registering mapper exam")
		return
	}
}

func CreateQuestion(c *fiber.Ctx) error {
	question := &dto.Question{}
	if err := c.BodyParser(question); err != nil {
		return result.ParamErrorResult(c, err)
	}
	id, err := service.CreateQuestion(question)
	return result.HttpResult(c, id, err)
}

func UpdateQuestion(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	question := &dto.Question{}
	if err := c.BodyParser(question); err != nil {
		return result.ParamErrorResult(c, err)
	}
	_, err = service.UpdateQuestion(id, question)
	return result.HttpResult(c, id, err)
}

func GetQuestionById(c *fiber.Ctx) error {
	id, err := c.ParamsInt("id")
	if err != nil {
		return result.ParamErrorResult(c, err)
	}
	question, err := service.GetQuestionById(id)
	if err != nil {
		return result.HttpResult(c, nil, err)
	}
	questionDto := dto.QuestionDto{}
	if err := questionDto.SetQuestionData(question.QuestionType, question.Data); err != nil {
		return err
	}

	mapper.SetEnableFieldIgnoreTag(true)
	if err = mapper.AutoMapper(question, &questionDto); err != nil {
		return result.HttpResult(c, nil, err)
	}
	return result.HttpResult(c, questionDto, err)
}
