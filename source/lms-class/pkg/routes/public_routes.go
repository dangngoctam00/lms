package routes

import (
	"github.com/gofiber/fiber/v2"
	"lms-class/app/web/controllers"
)

// PublicRoutes func for describe group of public routes.
func PublicRoutes(a *fiber.App) {
	// Create routes group.
	route := a.Group("/api/v1")

	route.Get("/questions/:id", controllers.GetQuestionById)

	route.Get("/exams/:id", controllers.GetExamById)
	route.Get("/exams/published/:id", controllers.GetPublishedExamById)
	route.Post("/exams", controllers.CreateExam)
	route.Put("/exams/:id", controllers.UpdateExam)
	route.Put("/exams/publish/:id", controllers.PublishExam)
	route.Post("/questions", controllers.CreateQuestion)
}
