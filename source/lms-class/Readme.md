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