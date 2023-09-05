//go:build ignore

package main

import (
	"entgo.io/ent/entc"
	"entgo.io/ent/entc/gen"
	"github.com/flume/enthistory"
	"log"
)

func main() {
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
}
