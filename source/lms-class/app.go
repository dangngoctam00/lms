package main

import (
	"github.com/gofiber/fiber/v2"
	"lms-class/pkg/configs"
	"lms-class/pkg/routes"
	"lms-class/pkg/utils"
)

func main() {
	config := configs.FiberConfig()
	app := fiber.New(config)

	routes.PublicRoutes(app)
	utils.StartServer(app)
}
