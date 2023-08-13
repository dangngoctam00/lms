package schema

import (
	"entgo.io/ent"
	"entgo.io/ent/dialect/entsql"
	"entgo.io/ent/schema"
	"entgo.io/ent/schema/edge"
	"entgo.io/ent/schema/field"
)

// Exam holds the schema definition for the Exam entity.
type Exam struct {
	ent.Schema
}

// Fields of the Exam.
func (Exam) Fields() []ent.Field {
	return []ent.Field{
		field.String("title"),
		field.String("context"),
		field.String("contextId"),
		field.Bool("isPublished"),
		field.Bool("havingDraft"),
		field.Time("lastPublishedAt").Optional().Nillable(),
		field.Time("updatedAt"),
	}
}

// Edges of the Exam.
func (Exam) Edges() []ent.Edge {
	return []ent.Edge{
		edge.To("quizzes", Quiz.Type),
	}
}

func (Exam) Annotations() []schema.Annotation {
	return []schema.Annotation{
		entsql.Annotation{Table: "exam"},
	}
}
