package schema

import (
	"encoding/json"
	"entgo.io/ent"
	"entgo.io/ent/dialect/entsql"
	"entgo.io/ent/schema"
	"entgo.io/ent/schema/edge"
	"entgo.io/ent/schema/field"
	"github.com/flume/enthistory"
	"lms-class/internal/web/dto/question"
)

// QuizSubmission holds the schema definition for the QuizSubmission entity.
type QuizSubmission struct {
	ent.Schema
}

// Fields of the QuizSubmission.
func (QuizSubmission) Fields() []ent.Field {
	return []ent.Field{
		field.Int("quizId"),
		field.Int("userId"),
		field.Time("startedAt"),
		field.Time("submittedAt").Optional().Nillable(),
		field.JSON("questions", json.RawMessage{}),
		field.JSON("answers", map[int][]question.Key{}).Optional(),
		field.Int("score").Optional().Nillable(),
	}
}

// Edges of the QuizSubmission.
func (QuizSubmission) Edges() []ent.Edge {
	return []ent.Edge{
		edge.From("quiz", Quiz.Type).
			Ref("submissions").
			Field("quizId").
			Required().
			Unique(),
	}
}

func (QuizSubmission) Annotations() []schema.Annotation {
	return []schema.Annotation{
		entsql.Annotation{Table: "quiz_submission"},
		enthistory.Annotations{
			// Exclude history tables for this schema
			Exclude: true,
		},
	}
}
