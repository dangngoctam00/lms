package utils

import (
	"context"
	"database/sql"
	"entgo.io/ent/dialect"
	entSql "entgo.io/ent/dialect/sql"
	entLms "lms-class/ent"
	"log"
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
	wrapperDriver := dialect.Debug(drv, func(a ...any) {
		log.Print(a)
	})
	EntClient = entLms.NewClient(entLms.Driver(wrapperDriver))
	EntClient.WithHistory()
	if err := EntClient.Schema.Create(context.Background()); err != nil {
		return err
	}
	return nil
}
