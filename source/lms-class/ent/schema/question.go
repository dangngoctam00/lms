package schema

import (
	"encoding/json"
	"entgo.io/ent"
	"entgo.io/ent/schema/field"
	"time"
)

// Question holds the schema definition for the Question entity.
type Question struct {
	ent.Schema
}

// Fields of the Question.
func (Question) Fields() []ent.Field {
	return []ent.Field{
		field.String("context"),
		field.Int("contextId"),
		field.Int("position"),
		field.String("questionType"),
		field.JSON("data", json.RawMessage{}),
		field.Time("updatedAt"),
		field.Int64("version").
			DefaultFunc(func() int64 {
				return time.Now().UnixNano()
			}).
			Comment("Unix time of when the latest update occurred"),
	}
}

// Edges of the Question.
func (Question) Edges() []ent.Edge {
	return nil
}
