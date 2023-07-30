package schema

import (
	"encoding/json"
	"entgo.io/ent"
	"entgo.io/ent/schema/field"
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
	}
}

// Edges of the Question.
func (Question) Edges() []ent.Edge {
	return nil
}
