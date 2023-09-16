package schema

import (
	"entgo.io/ent"
	"entgo.io/ent/dialect/entsql"
	"entgo.io/ent/schema"
	"entgo.io/ent/schema/edge"
	"entgo.io/ent/schema/field"
	"github.com/flume/enthistory"
)

// Quiz holds the schema definition for the Quiz entity.
type Quiz struct {
	ent.Schema
}

// Fields of the Quiz.
func (Quiz) Fields() []ent.Field {
	return []ent.Field{
		field.String("title"),
		field.String("description"),
		field.String("gradeTag"),
		field.Int("examId").Optional().Nillable(),
		field.Bool("isPublished"),
		field.Time("createdAt"),
		field.Time("updatedAt"),
		field.String("context"),
		field.Int("contextId"),
		field.Int("parentId").Optional().Nillable(),
		field.Time("startedAt").Optional().Nillable(),
		field.Time("finishedAt").Optional().Nillable(),
		field.Int("timeLimit").Optional().Nillable(),
		field.Int("maxAttempt").Optional().Nillable(),
		field.Bool("viewPreviousSessions"),
		field.Time("viewPreviousSessionsTime").Optional().Nillable(),
		field.Bool("viewResult"),
		field.Int("passedScore").Optional().Nillable(),
		field.String("finalGradedStrategy").Optional().Nillable(),
	}
}

// Edges of the Quiz.
func (Quiz) Edges() []ent.Edge {
	return []ent.Edge{
		edge.From("exam", Exam.Type).
			Ref("quizzes").
			Field("examId").
			Unique(),
		edge.To("submissions", QuizSubmission.Type),
	}
}

func (Quiz) Annotations() []schema.Annotation {
	return []schema.Annotation{
		enthistory.Annotations{
			// Exclude history tables for this schema
			Exclude: true,
		},
		entsql.Annotation{Table: "quiz"},
	}
}
