// Code generated by ent, DO NOT EDIT.

package migrate

import (
	"entgo.io/ent/dialect/entsql"
	"entgo.io/ent/dialect/sql/schema"
	"entgo.io/ent/schema/field"
)

var (
	// ExamColumns holds the columns for the "exam" table.
	ExamColumns = []*schema.Column{
		{Name: "id", Type: field.TypeInt, Increment: true},
		{Name: "title", Type: field.TypeString},
		{Name: "context", Type: field.TypeString},
		{Name: "context_id", Type: field.TypeString},
		{Name: "is_published", Type: field.TypeBool},
		{Name: "having_draft", Type: field.TypeBool},
		{Name: "last_published_at", Type: field.TypeTime, Nullable: true},
		{Name: "updated_at", Type: field.TypeTime},
	}
	// ExamTable holds the schema information for the "exam" table.
	ExamTable = &schema.Table{
		Name:       "exam",
		Columns:    ExamColumns,
		PrimaryKey: []*schema.Column{ExamColumns[0]},
	}
	// ExamHistoryColumns holds the columns for the "exam_history" table.
	ExamHistoryColumns = []*schema.Column{
		{Name: "id", Type: field.TypeInt, Increment: true},
		{Name: "history_time", Type: field.TypeTime},
		{Name: "operation", Type: field.TypeEnum, Enums: []string{"INSERT", "UPDATE", "DELETE"}},
		{Name: "ref", Type: field.TypeInt, Nullable: true},
		{Name: "updated_by", Type: field.TypeInt, Nullable: true},
		{Name: "title", Type: field.TypeString},
		{Name: "context", Type: field.TypeString},
		{Name: "context_id", Type: field.TypeString},
		{Name: "is_published", Type: field.TypeBool},
		{Name: "having_draft", Type: field.TypeBool},
		{Name: "last_published_at", Type: field.TypeTime, Nullable: true},
		{Name: "updated_at", Type: field.TypeTime},
	}
	// ExamHistoryTable holds the schema information for the "exam_history" table.
	ExamHistoryTable = &schema.Table{
		Name:       "exam_history",
		Columns:    ExamHistoryColumns,
		PrimaryKey: []*schema.Column{ExamHistoryColumns[0]},
	}
	// QuestionColumns holds the columns for the "question" table.
	QuestionColumns = []*schema.Column{
		{Name: "id", Type: field.TypeInt, Increment: true},
		{Name: "context", Type: field.TypeString},
		{Name: "context_id", Type: field.TypeInt},
		{Name: "position", Type: field.TypeInt},
		{Name: "question_type", Type: field.TypeString},
		{Name: "data", Type: field.TypeJSON},
		{Name: "updated_at", Type: field.TypeTime},
		{Name: "version", Type: field.TypeInt64},
	}
	// QuestionTable holds the schema information for the "question" table.
	QuestionTable = &schema.Table{
		Name:       "question",
		Columns:    QuestionColumns,
		PrimaryKey: []*schema.Column{QuestionColumns[0]},
	}
	// QuestionHistoryColumns holds the columns for the "question_history" table.
	QuestionHistoryColumns = []*schema.Column{
		{Name: "id", Type: field.TypeInt, Increment: true},
		{Name: "history_time", Type: field.TypeTime},
		{Name: "operation", Type: field.TypeEnum, Enums: []string{"INSERT", "UPDATE", "DELETE"}},
		{Name: "ref", Type: field.TypeInt, Nullable: true},
		{Name: "updated_by", Type: field.TypeInt, Nullable: true},
		{Name: "context", Type: field.TypeString},
		{Name: "context_id", Type: field.TypeInt},
		{Name: "position", Type: field.TypeInt},
		{Name: "question_type", Type: field.TypeString},
		{Name: "data", Type: field.TypeJSON},
		{Name: "updated_at", Type: field.TypeTime},
		{Name: "version", Type: field.TypeInt64},
	}
	// QuestionHistoryTable holds the schema information for the "question_history" table.
	QuestionHistoryTable = &schema.Table{
		Name:       "question_history",
		Columns:    QuestionHistoryColumns,
		PrimaryKey: []*schema.Column{QuestionHistoryColumns[0]},
	}
	// QuizColumns holds the columns for the "quiz" table.
	QuizColumns = []*schema.Column{
		{Name: "id", Type: field.TypeInt, Increment: true},
		{Name: "title", Type: field.TypeString},
		{Name: "description", Type: field.TypeString},
		{Name: "grade_tag", Type: field.TypeString},
		{Name: "is_published", Type: field.TypeBool},
		{Name: "created_at", Type: field.TypeTime},
		{Name: "updated_at", Type: field.TypeTime},
		{Name: "context", Type: field.TypeString},
		{Name: "context_id", Type: field.TypeInt},
		{Name: "parent_id", Type: field.TypeInt, Nullable: true},
		{Name: "started_at", Type: field.TypeTime, Nullable: true},
		{Name: "finished_at", Type: field.TypeTime, Nullable: true},
		{Name: "time_limit", Type: field.TypeInt, Nullable: true},
		{Name: "max_attempt", Type: field.TypeInt, Nullable: true},
		{Name: "view_previous_sessions", Type: field.TypeBool},
		{Name: "view_previous_sessions_time", Type: field.TypeTime, Nullable: true},
		{Name: "view_result", Type: field.TypeBool},
		{Name: "passed_score", Type: field.TypeInt, Nullable: true},
		{Name: "final_graded_strategy", Type: field.TypeString, Nullable: true},
		{Name: "exam_id", Type: field.TypeInt, Nullable: true},
	}
	// QuizTable holds the schema information for the "quiz" table.
	QuizTable = &schema.Table{
		Name:       "quiz",
		Columns:    QuizColumns,
		PrimaryKey: []*schema.Column{QuizColumns[0]},
		ForeignKeys: []*schema.ForeignKey{
			{
				Symbol:     "quiz_exam_quizzes",
				Columns:    []*schema.Column{QuizColumns[19]},
				RefColumns: []*schema.Column{ExamColumns[0]},
				OnDelete:   schema.SetNull,
			},
		},
	}
	// QuizSubmissionColumns holds the columns for the "quiz_submission" table.
	QuizSubmissionColumns = []*schema.Column{
		{Name: "id", Type: field.TypeInt, Increment: true},
		{Name: "user_id", Type: field.TypeInt},
		{Name: "started_at", Type: field.TypeTime},
		{Name: "submitted_at", Type: field.TypeTime, Nullable: true},
		{Name: "questions", Type: field.TypeJSON},
		{Name: "answers", Type: field.TypeJSON, Nullable: true},
		{Name: "score", Type: field.TypeInt, Nullable: true},
		{Name: "quiz_id", Type: field.TypeInt},
	}
	// QuizSubmissionTable holds the schema information for the "quiz_submission" table.
	QuizSubmissionTable = &schema.Table{
		Name:       "quiz_submission",
		Columns:    QuizSubmissionColumns,
		PrimaryKey: []*schema.Column{QuizSubmissionColumns[0]},
		ForeignKeys: []*schema.ForeignKey{
			{
				Symbol:     "quiz_submission_quiz_submissions",
				Columns:    []*schema.Column{QuizSubmissionColumns[7]},
				RefColumns: []*schema.Column{QuizColumns[0]},
				OnDelete:   schema.NoAction,
			},
		},
	}
	// Tables holds all the tables in the schema.
	Tables = []*schema.Table{
		ExamTable,
		ExamHistoryTable,
		QuestionTable,
		QuestionHistoryTable,
		QuizTable,
		QuizSubmissionTable,
	}
)

func init() {
	ExamTable.Annotation = &entsql.Annotation{
		Table: "exam",
	}
	ExamHistoryTable.Annotation = &entsql.Annotation{
		Table: "exam_history",
	}
	QuestionTable.Annotation = &entsql.Annotation{
		Table: "question",
	}
	QuestionHistoryTable.Annotation = &entsql.Annotation{
		Table: "question_history",
	}
	QuizTable.ForeignKeys[0].RefTable = ExamTable
	QuizTable.Annotation = &entsql.Annotation{
		Table: "quiz",
	}
	QuizSubmissionTable.ForeignKeys[0].RefTable = QuizTable
	QuizSubmissionTable.Annotation = &entsql.Annotation{
		Table: "quiz_submission",
	}
}
