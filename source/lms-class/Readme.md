# Lms class module

## ent

Run the following command to generate code.

```bash
    go generate ./ent
```

Instead of using command line to generate `ent` code, we create go code block:

```go
    if err := entc.Generate("./schema",
        &gen.Config{Features: []gen.Feature{gen.FeatureLock, gen.FeatureIntercept, gen.FeatureSnapshot}},
        entc.Extensions(
            enthistory.NewHistoryExtension(
            enthistory.WithUpdatedBy("userId", enthistory.ValueTypeInt),
                enthistory.WithAuditing(),
            ),
        ),
    ); err != nil {
        log.Fatal("running ent codegen:", err)
    }
	
```

## Tenant migrate and query (PostgreSQL)

### Migrate

Use driver directly to execute sql statement:

```bash
    set search_path=<schema>
```

```go
    _ = drv.Exec(context.Background(), fmt.Sprintf("set search_path=%s", tenant), []any{}, nil)
```
### Query

Use **sql.Selector** to set target schema before executing query:

```go
    func GetPublishedExam(id int) (*ent.ExamHistory, error) {
        latest, err := utils.EntClient.ExamHistory.Query().
            Where(func(s *sql.Selector) {
                t := sql.Table(examhistory.Table)
                t.Schema(pkg.GetTenant())
                s.From(t).Select().
                    Where(sql.EQ(t.C(examhistory.FieldRef), id)).
                    Where(sql.IsFalse(t.C(examhistory.FieldHavingDraft)))
        }).Latest(context.Background())
        return latest, err
    }
```