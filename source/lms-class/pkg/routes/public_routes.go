package routes

import (
	"github.com/gofiber/fiber/v2"
	"lms-class/internal/pkg/exam/controller"
	examController "lms-class/internal/pkg/exam/controller"
	questionController "lms-class/internal/pkg/question/controller"
	quizController "lms-class/internal/pkg/quiz/controller"
)

func PublicRoutes(a *fiber.App) {
	route := a.Group("/api/v1")

	route.Get("/questions/:id", questionController.GetQuestionById)
	route.Post("/questions", questionController.CreateQuestion)
	route.Put("/questions/:id", questionController.UpdateQuestion)

	route.Get("/exams/:id", controller.GetExamById)
	route.Get("/exams/published/:id", controller.GetPublishedExamById)
	route.Post("/exams", examController.CreateExam)
	route.Put("/exams/:id", examController.UpdateExam)
	route.Put("/exams/publish/:id", examController.PublishExam)

	route.Post("/quizzes", quizController.CreateQuiz)
	route.Get("/quizzes/:id", quizController.GetQuizById)
	route.Post("/quizzes/:id/do", quizController.DoQuiz)
	route.Get("/quizzes/sessions/:id", quizController.GetQuizSessionById)
	route.Post("/quizzes/sessions/:sessionId/questions/:questionId", quizController.AnswerQuestionById)
}
