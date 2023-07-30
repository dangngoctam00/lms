package utils

import (
	"context"
	"database/sql"
	entSql "entgo.io/ent/dialect/sql"
	entLms "lms-class/ent"
	"os"

	_ "github.com/lib/pq"
)

var EntClient *entLms.Client

func InitDatabaseExtensions() error {
	var db *sql.DB
	db, err := sql.Open("postgres", os.Getenv("DB_SERVER_URL"))

	if err != nil {
		return err
	}
	db.SetMaxOpenConns(100)
	db.SetMaxIdleConns(50)
	drv := entSql.OpenDB("postgres", db)
	EntClient = entLms.NewClient(entLms.Driver(drv))
	EntClient.WithHistory()
	if err := EntClient.Schema.Create(context.Background()); err != nil {
		return err
	}
	return nil
}
