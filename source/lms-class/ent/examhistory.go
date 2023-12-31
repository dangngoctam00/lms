// Code generated by ent, DO NOT EDIT.

package ent

import (
	"fmt"
	"lms-class/ent/examhistory"
	"strings"
	"time"

	"entgo.io/ent"
	"entgo.io/ent/dialect/sql"
	"github.com/flume/enthistory"
)

// ExamHistory is the model entity for the ExamHistory schema.
type ExamHistory struct {
	config `json:"-"`
	// ID of the ent.
	ID int `json:"id,omitempty"`
	// HistoryTime holds the value of the "history_time" field.
	HistoryTime time.Time `json:"history_time,omitempty"`
	// Operation holds the value of the "operation" field.
	Operation enthistory.OpType `json:"operation,omitempty"`
	// Ref holds the value of the "ref" field.
	Ref int `json:"ref,omitempty"`
	// UpdatedBy holds the value of the "updated_by" field.
	UpdatedBy *int `json:"updated_by,omitempty"`
	// Title holds the value of the "title" field.
	Title string `json:"title,omitempty"`
	// Context holds the value of the "context" field.
	Context string `json:"context,omitempty"`
	// ContextId holds the value of the "contextId" field.
	ContextId string `json:"contextId,omitempty"`
	// IsPublished holds the value of the "isPublished" field.
	IsPublished bool `json:"isPublished,omitempty"`
	// HavingDraft holds the value of the "havingDraft" field.
	HavingDraft bool `json:"havingDraft,omitempty"`
	// LastPublishedAt holds the value of the "lastPublishedAt" field.
	LastPublishedAt *time.Time `json:"lastPublishedAt,omitempty"`
	// UpdatedAt holds the value of the "updatedAt" field.
	UpdatedAt    time.Time `json:"updatedAt,omitempty"`
	selectValues sql.SelectValues
}

// scanValues returns the types for scanning values from sql.Rows.
func (*ExamHistory) scanValues(columns []string) ([]any, error) {
	values := make([]any, len(columns))
	for i := range columns {
		switch columns[i] {
		case examhistory.FieldIsPublished, examhistory.FieldHavingDraft:
			values[i] = new(sql.NullBool)
		case examhistory.FieldID, examhistory.FieldRef, examhistory.FieldUpdatedBy:
			values[i] = new(sql.NullInt64)
		case examhistory.FieldOperation, examhistory.FieldTitle, examhistory.FieldContext, examhistory.FieldContextId:
			values[i] = new(sql.NullString)
		case examhistory.FieldHistoryTime, examhistory.FieldLastPublishedAt, examhistory.FieldUpdatedAt:
			values[i] = new(sql.NullTime)
		default:
			values[i] = new(sql.UnknownType)
		}
	}
	return values, nil
}

// assignValues assigns the values that were returned from sql.Rows (after scanning)
// to the ExamHistory fields.
func (eh *ExamHistory) assignValues(columns []string, values []any) error {
	if m, n := len(values), len(columns); m < n {
		return fmt.Errorf("mismatch number of scan values: %d != %d", m, n)
	}
	for i := range columns {
		switch columns[i] {
		case examhistory.FieldID:
			value, ok := values[i].(*sql.NullInt64)
			if !ok {
				return fmt.Errorf("unexpected type %T for field id", value)
			}
			eh.ID = int(value.Int64)
		case examhistory.FieldHistoryTime:
			if value, ok := values[i].(*sql.NullTime); !ok {
				return fmt.Errorf("unexpected type %T for field history_time", values[i])
			} else if value.Valid {
				eh.HistoryTime = value.Time
			}
		case examhistory.FieldOperation:
			if value, ok := values[i].(*sql.NullString); !ok {
				return fmt.Errorf("unexpected type %T for field operation", values[i])
			} else if value.Valid {
				eh.Operation = enthistory.OpType(value.String)
			}
		case examhistory.FieldRef:
			if value, ok := values[i].(*sql.NullInt64); !ok {
				return fmt.Errorf("unexpected type %T for field ref", values[i])
			} else if value.Valid {
				eh.Ref = int(value.Int64)
			}
		case examhistory.FieldUpdatedBy:
			if value, ok := values[i].(*sql.NullInt64); !ok {
				return fmt.Errorf("unexpected type %T for field updated_by", values[i])
			} else if value.Valid {
				eh.UpdatedBy = new(int)
				*eh.UpdatedBy = int(value.Int64)
			}
		case examhistory.FieldTitle:
			if value, ok := values[i].(*sql.NullString); !ok {
				return fmt.Errorf("unexpected type %T for field title", values[i])
			} else if value.Valid {
				eh.Title = value.String
			}
		case examhistory.FieldContext:
			if value, ok := values[i].(*sql.NullString); !ok {
				return fmt.Errorf("unexpected type %T for field context", values[i])
			} else if value.Valid {
				eh.Context = value.String
			}
		case examhistory.FieldContextId:
			if value, ok := values[i].(*sql.NullString); !ok {
				return fmt.Errorf("unexpected type %T for field contextId", values[i])
			} else if value.Valid {
				eh.ContextId = value.String
			}
		case examhistory.FieldIsPublished:
			if value, ok := values[i].(*sql.NullBool); !ok {
				return fmt.Errorf("unexpected type %T for field isPublished", values[i])
			} else if value.Valid {
				eh.IsPublished = value.Bool
			}
		case examhistory.FieldHavingDraft:
			if value, ok := values[i].(*sql.NullBool); !ok {
				return fmt.Errorf("unexpected type %T for field havingDraft", values[i])
			} else if value.Valid {
				eh.HavingDraft = value.Bool
			}
		case examhistory.FieldLastPublishedAt:
			if value, ok := values[i].(*sql.NullTime); !ok {
				return fmt.Errorf("unexpected type %T for field lastPublishedAt", values[i])
			} else if value.Valid {
				eh.LastPublishedAt = new(time.Time)
				*eh.LastPublishedAt = value.Time
			}
		case examhistory.FieldUpdatedAt:
			if value, ok := values[i].(*sql.NullTime); !ok {
				return fmt.Errorf("unexpected type %T for field updatedAt", values[i])
			} else if value.Valid {
				eh.UpdatedAt = value.Time
			}
		default:
			eh.selectValues.Set(columns[i], values[i])
		}
	}
	return nil
}

// Value returns the ent.Value that was dynamically selected and assigned to the ExamHistory.
// This includes values selected through modifiers, order, etc.
func (eh *ExamHistory) Value(name string) (ent.Value, error) {
	return eh.selectValues.Get(name)
}

// Update returns a builder for updating this ExamHistory.
// Note that you need to call ExamHistory.Unwrap() before calling this method if this ExamHistory
// was returned from a transaction, and the transaction was committed or rolled back.
func (eh *ExamHistory) Update() *ExamHistoryUpdateOne {
	return NewExamHistoryClient(eh.config).UpdateOne(eh)
}

// Unwrap unwraps the ExamHistory entity that was returned from a transaction after it was closed,
// so that all future queries will be executed through the driver which created the transaction.
func (eh *ExamHistory) Unwrap() *ExamHistory {
	_tx, ok := eh.config.driver.(*txDriver)
	if !ok {
		panic("ent: ExamHistory is not a transactional entity")
	}
	eh.config.driver = _tx.drv
	return eh
}

// String implements the fmt.Stringer.
func (eh *ExamHistory) String() string {
	var builder strings.Builder
	builder.WriteString("ExamHistory(")
	builder.WriteString(fmt.Sprintf("id=%v, ", eh.ID))
	builder.WriteString("history_time=")
	builder.WriteString(eh.HistoryTime.Format(time.ANSIC))
	builder.WriteString(", ")
	builder.WriteString("operation=")
	builder.WriteString(fmt.Sprintf("%v", eh.Operation))
	builder.WriteString(", ")
	builder.WriteString("ref=")
	builder.WriteString(fmt.Sprintf("%v", eh.Ref))
	builder.WriteString(", ")
	if v := eh.UpdatedBy; v != nil {
		builder.WriteString("updated_by=")
		builder.WriteString(fmt.Sprintf("%v", *v))
	}
	builder.WriteString(", ")
	builder.WriteString("title=")
	builder.WriteString(eh.Title)
	builder.WriteString(", ")
	builder.WriteString("context=")
	builder.WriteString(eh.Context)
	builder.WriteString(", ")
	builder.WriteString("contextId=")
	builder.WriteString(eh.ContextId)
	builder.WriteString(", ")
	builder.WriteString("isPublished=")
	builder.WriteString(fmt.Sprintf("%v", eh.IsPublished))
	builder.WriteString(", ")
	builder.WriteString("havingDraft=")
	builder.WriteString(fmt.Sprintf("%v", eh.HavingDraft))
	builder.WriteString(", ")
	if v := eh.LastPublishedAt; v != nil {
		builder.WriteString("lastPublishedAt=")
		builder.WriteString(v.Format(time.ANSIC))
	}
	builder.WriteString(", ")
	builder.WriteString("updatedAt=")
	builder.WriteString(eh.UpdatedAt.Format(time.ANSIC))
	builder.WriteByte(')')
	return builder.String()
}

// ExamHistories is a parsable slice of ExamHistory.
type ExamHistories []*ExamHistory
