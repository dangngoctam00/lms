package utils

import (
	"context"
	"database/sql"
	"entgo.io/ent/dialect"
	entSql "entgo.io/ent/dialect/sql"
	"fmt"
	entLms "lms-class/ent"
	"lms-class/pkg"
	"log"
	"os"
	"sync"

	_ "github.com/lib/pq"
)

var EntClient *entLms.Client

var syncOnce sync.Once

func InitDatabaseExtensions() error {
	var err error
	syncOnce.Do(func() {
		var db *sql.DB
		db, err = sql.Open("postgres", os.Getenv("DB_SERVER_URL"))
		if err != nil {
			return
		}
		db.SetMaxOpenConns(100)
		db.SetMaxIdleConns(50)
		drv := entSql.OpenDB("postgres", db)
		// Auto migrate predefined tenants
		// TODO
		tenants := []string{"ngman", "ngtam"}
		for _, tenant := range tenants {
			_ = drv.Exec(context.Background(), fmt.Sprintf("set search_path=%s", tenant), []any{}, nil)
			wrapperDriver := dialect.Debug(drv, func(a ...any) {
				log.Print(a)
			})
			EntClient = entLms.NewClient(entLms.Driver(wrapperDriver))
			EntClient.WithHistory()
			// TODO: extract tenant from request instead of hard coding value
			pkg.LmsContext = context.WithValue(context.Background(), pkg.SchemaKey, "ngman")
			err = EntClient.Schema.Create(pkg.LmsContext)
		}
	})
	return err
}
