// Code generated by ent, DO NOT EDIT.

package ent

import (
	"encoding/json"
	"fmt"
	"lms-class/ent/quiz"
	"lms-class/ent/quizsubmission"
	"lms-class/internal/pkg/question/dto"
	"strings"
	"time"

	"entgo.io/ent"
	"entgo.io/ent/dialect/sql"
)

// QuizSubmission is the model entity for the QuizSubmission schema.
type QuizSubmission struct {
	config `json:"-"`
	// ID of the ent.
	ID int `json:"id,omitempty"`
	// QuizId holds the value of the "quizId" field.
	QuizId int `json:"quizId,omitempty"`
	// UserId holds the value of the "userId" field.
	UserId int `json:"userId,omitempty"`
	// StartedAt holds the value of the "startedAt" field.
	StartedAt time.Time `json:"startedAt,omitempty"`
	// SubmittedAt holds the value of the "submittedAt" field.
	SubmittedAt *time.Time `json:"submittedAt,omitempty"`
	// Questions holds the value of the "questions" field.
	Questions json.RawMessage `json:"questions,omitempty"`
	// Answers holds the value of the "answers" field.
	Answers map[int][]dto.Key `json:"answers,omitempty"`
	// Score holds the value of the "score" field.
	Score *int `json:"score,omitempty"`
	// Edges holds the relations/edges for other nodes in the graph.
	// The values are being populated by the QuizSubmissionQuery when eager-loading is set.
	Edges        QuizSubmissionEdges `json:"edges"`
	selectValues sql.SelectValues
}

// QuizSubmissionEdges holds the relations/edges for other nodes in the graph.
type QuizSubmissionEdges struct {
	// Quiz holds the value of the quiz edge.
	Quiz *Quiz `json:"quiz,omitempty"`
	// loadedTypes holds the information for reporting if a
	// type was loaded (or requested) in eager-loading or not.
	loadedTypes [1]bool
}

// QuizOrErr returns the Quiz value or an error if the edge
// was not loaded in eager-loading, or loaded but was not found.
func (e QuizSubmissionEdges) QuizOrErr() (*Quiz, error) {
	if e.loadedTypes[0] {
		if e.Quiz == nil {
			// Edge was loaded but was not found.
			return nil, &NotFoundError{label: quiz.Label}
		}
		return e.Quiz, nil
	}
	return nil, &NotLoadedError{edge: "quiz"}
}

// scanValues returns the types for scanning values from sql.Rows.
func (*QuizSubmission) scanValues(columns []string) ([]any, error) {
	values := make([]any, len(columns))
	for i := range columns {
		switch columns[i] {
		case quizsubmission.FieldQuestions, quizsubmission.FieldAnswers:
			values[i] = new([]byte)
		case quizsubmission.FieldID, quizsubmission.FieldQuizId, quizsubmission.FieldUserId, quizsubmission.FieldScore:
			values[i] = new(sql.NullInt64)
		case quizsubmission.FieldStartedAt, quizsubmission.FieldSubmittedAt:
			values[i] = new(sql.NullTime)
		default:
			values[i] = new(sql.UnknownType)
		}
	}
	return values, nil
}

// assignValues assigns the values that were returned from sql.Rows (after scanning)
// to the QuizSubmission fields.
func (qs *QuizSubmission) assignValues(columns []string, values []any) error {
	if m, n := len(values), len(columns); m < n {
		return fmt.Errorf("mismatch number of scan values: %d != %d", m, n)
	}
	for i := range columns {
		switch columns[i] {
		case quizsubmission.FieldID:
			value, ok := values[i].(*sql.NullInt64)
			if !ok {
				return fmt.Errorf("unexpected type %T for field id", value)
			}
			qs.ID = int(value.Int64)
		case quizsubmission.FieldQuizId:
			if value, ok := values[i].(*sql.NullInt64); !ok {
				return fmt.Errorf("unexpected type %T for field quizId", values[i])
			} else if value.Valid {
				qs.QuizId = int(value.Int64)
			}
		case quizsubmission.FieldUserId:
			if value, ok := values[i].(*sql.NullInt64); !ok {
				return fmt.Errorf("unexpected type %T for field userId", values[i])
			} else if value.Valid {
				qs.UserId = int(value.Int64)
			}
		case quizsubmission.FieldStartedAt:
			if value, ok := values[i].(*sql.NullTime); !ok {
				return fmt.Errorf("unexpected type %T for field startedAt", values[i])
			} else if value.Valid {
				qs.StartedAt = value.Time
			}
		case quizsubmission.FieldSubmittedAt:
			if value, ok := values[i].(*sql.NullTime); !ok {
				return fmt.Errorf("unexpected type %T for field submittedAt", values[i])
			} else if value.Valid {
				qs.SubmittedAt = new(time.Time)
				*qs.SubmittedAt = value.Time
			}
		case quizsubmission.FieldQuestions:
			if value, ok := values[i].(*[]byte); !ok {
				return fmt.Errorf("unexpected type %T for field questions", values[i])
			} else if value != nil && len(*value) > 0 {
				if err := json.Unmarshal(*value, &qs.Questions); err != nil {
					return fmt.Errorf("unmarshal field questions: %w", err)
				}
			}
		case quizsubmission.FieldAnswers:
			if value, ok := values[i].(*[]byte); !ok {
				return fmt.Errorf("unexpected type %T for field answers", values[i])
			} else if value != nil && len(*value) > 0 {
				if err := json.Unmarshal(*value, &qs.Answers); err != nil {
					return fmt.Errorf("unmarshal field answers: %w", err)
				}
			}
		case quizsubmission.FieldScore:
			if value, ok := values[i].(*sql.NullInt64); !ok {
				return fmt.Errorf("unexpected type %T for field score", values[i])
			} else if value.Valid {
				qs.Score = new(int)
				*qs.Score = int(value.Int64)
			}
		default:
			qs.selectValues.Set(columns[i], values[i])
		}
	}
	return nil
}

// Value returns the ent.Value that was dynamically selected and assigned to the QuizSubmission.
// This includes values selected through modifiers, order, etc.
func (qs *QuizSubmission) Value(name string) (ent.Value, error) {
	return qs.selectValues.Get(name)
}

// QueryQuiz queries the "quiz" edge of the QuizSubmission entity.
func (qs *QuizSubmission) QueryQuiz() *QuizQuery {
	return NewQuizSubmissionClient(qs.config).QueryQuiz(qs)
}

// Update returns a builder for updating this QuizSubmission.
// Note that you need to call QuizSubmission.Unwrap() before calling this method if this QuizSubmission
// was returned from a transaction, and the transaction was committed or rolled back.
func (qs *QuizSubmission) Update() *QuizSubmissionUpdateOne {
	return NewQuizSubmissionClient(qs.config).UpdateOne(qs)
}

// Unwrap unwraps the QuizSubmission entity that was returned from a transaction after it was closed,
// so that all future queries will be executed through the driver which created the transaction.
func (qs *QuizSubmission) Unwrap() *QuizSubmission {
	_tx, ok := qs.config.driver.(*txDriver)
	if !ok {
		panic("ent: QuizSubmission is not a transactional entity")
	}
	qs.config.driver = _tx.drv
	return qs
}

// String implements the fmt.Stringer.
func (qs *QuizSubmission) String() string {
	var builder strings.Builder
	builder.WriteString("QuizSubmission(")
	builder.WriteString(fmt.Sprintf("id=%v, ", qs.ID))
	builder.WriteString("quizId=")
	builder.WriteString(fmt.Sprintf("%v", qs.QuizId))
	builder.WriteString(", ")
	builder.WriteString("userId=")
	builder.WriteString(fmt.Sprintf("%v", qs.UserId))
	builder.WriteString(", ")
	builder.WriteString("startedAt=")
	builder.WriteString(qs.StartedAt.Format(time.ANSIC))
	builder.WriteString(", ")
	if v := qs.SubmittedAt; v != nil {
		builder.WriteString("submittedAt=")
		builder.WriteString(v.Format(time.ANSIC))
	}
	builder.WriteString(", ")
	builder.WriteString("questions=")
	builder.WriteString(fmt.Sprintf("%v", qs.Questions))
	builder.WriteString(", ")
	builder.WriteString("answers=")
	builder.WriteString(fmt.Sprintf("%v", qs.Answers))
	builder.WriteString(", ")
	if v := qs.Score; v != nil {
		builder.WriteString("score=")
		builder.WriteString(fmt.Sprintf("%v", *v))
	}
	builder.WriteByte(')')
	return builder.String()
}

// QuizSubmissions is a parsable slice of QuizSubmission.
type QuizSubmissions []*QuizSubmission
