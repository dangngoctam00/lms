package routes

import (
	"github.com/gofiber/fiber/v2"
	"lms-class/internal/web/controllers"
)

func PublicRoutes(a *fiber.App) {
	route := a.Group("/api/v1")

	route.Get("/questions/:id", controllers.GetQuestionById)

	route.Get("/exams/:id", controllers.GetExamById)
	route.Get("/exams/published/:id", controllers.GetPublishedExamById)
	route.Post("/exams", controllers.CreateExam)
	route.Put("/exams/:id", controllers.UpdateExam)
	route.Put("/exams/publish/:id", controllers.PublishExam)
	route.Post("/questions", controllers.CreateQuestion)
	route.Put("/questions/:id", controllers.UpdateQuestion)

	route.Post("/quizzes", controllers.CreateQuiz)
	route.Get("/quizzes/:id", controllers.GetQuizById)
	route.Post("/quizzes/:id/do", controllers.DoQuiz)
	route.Get("/quizzes/sessions/:id", controllers.GetQuizSessionById)
}
