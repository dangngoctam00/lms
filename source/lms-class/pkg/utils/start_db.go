package utils

import (
	"context"
	"database/sql"
	"entgo.io/ent"
	"entgo.io/ent/dialect"
	entSql "entgo.io/ent/dialect/sql"
	"fmt"
	_ "github.com/lib/pq"
	entLms "lms-class/ent"
	"lms-class/ent/intercept"
	"lms-class/pkg"
	"log"
	"os"
	"sync"
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
			// TODO: extract tenant from request instead of hard coding value
			pkg.LmsContext = context.WithValue(context.Background(), pkg.SchemaKey, "ngtam")
			EntClient.Intercept(ent.InterceptFunc(func(next ent.Querier) ent.Querier {
				return ent.QuerierFunc(func(ctx context.Context, query ent.Query) (ent.Value, error) {
					q, err := intercept.NewQuery(query)
					q.WhereP(func(selector *entSql.Selector) {
						selector.Table().Schema(pkg.GetTenant())
					})
					// Do something before the query execution.
					value, err := next.Query(ctx, query)
					// Do something after the query execution.
					return value, err
				})
			}))

			// TODO: Doesn't work

			//EntClient.Use(func(next ent.Mutator) ent.Mutator {
			//	return ent.MutateFunc(func(ctx context.Context, m ent.Mutation) (ent.Value, error) {
			//		if m, ok := m.(*entLms.ExamMutation); ok {
			//			m.WhereP(func(selector *entSql.Selector) {
			//				selector.Table().Schema(pkg.GetTenant())
			//			})
			//		}
			//		if m, ok := m.(*entLms.ExamHistoryMutation); ok {
			//			m.WhereP(func(selector *entSql.Selector) {
			//				selector.Table().Schema(pkg.GetTenant())
			//			})
			//		}
			//		return next.Mutate(ctx, m)
			//	})
			//})

			EntClient.WithHistory()
			err = EntClient.Schema.Create(pkg.LmsContext)
		}
		_ = drv.Exec(context.Background(), "set search_path=", []any{}, nil)
	})
	return err
}
