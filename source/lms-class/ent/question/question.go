// Code generated by ent, DO NOT EDIT.

package question

import (
	"entgo.io/ent/dialect/sql"
)

const (
	// Label holds the string label denoting the question type in the database.
	Label = "question"
	// FieldID holds the string denoting the id field in the database.
	FieldID = "id"
	// FieldContext holds the string denoting the context field in the database.
	FieldContext = "context"
	// FieldContextId holds the string denoting the contextid field in the database.
	FieldContextId = "context_id"
	// FieldPosition holds the string denoting the position field in the database.
	FieldPosition = "position"
	// FieldQuestionType holds the string denoting the questiontype field in the database.
	FieldQuestionType = "question_type"
	// FieldData holds the string denoting the data field in the database.
	FieldData = "data"
	// FieldUpdatedAt holds the string denoting the updatedat field in the database.
	FieldUpdatedAt = "updated_at"
	// Table holds the table name of the question in the database.
	Table = "questions"
)

// Columns holds all SQL columns for question fields.
var Columns = []string{
	FieldID,
	FieldContext,
	FieldContextId,
	FieldPosition,
	FieldQuestionType,
	FieldData,
	FieldUpdatedAt,
}

// ValidColumn reports if the column name is valid (part of the table columns).
func ValidColumn(column string) bool {
	for i := range Columns {
		if column == Columns[i] {
			return true
		}
	}
	return false
}

// OrderOption defines the ordering options for the Question queries.
type OrderOption func(*sql.Selector)

// ByID orders the results by the id field.
func ByID(opts ...sql.OrderTermOption) OrderOption {
	return sql.OrderByField(FieldID, opts...).ToFunc()
}

// ByContext orders the results by the context field.
func ByContext(opts ...sql.OrderTermOption) OrderOption {
	return sql.OrderByField(FieldContext, opts...).ToFunc()
}

// ByContextId orders the results by the contextId field.
func ByContextId(opts ...sql.OrderTermOption) OrderOption {
	return sql.OrderByField(FieldContextId, opts...).ToFunc()
}

// ByPosition orders the results by the position field.
func ByPosition(opts ...sql.OrderTermOption) OrderOption {
	return sql.OrderByField(FieldPosition, opts...).ToFunc()
}

// ByQuestionType orders the results by the questionType field.
func ByQuestionType(opts ...sql.OrderTermOption) OrderOption {
	return sql.OrderByField(FieldQuestionType, opts...).ToFunc()
}

// ByUpdatedAt orders the results by the updatedAt field.
func ByUpdatedAt(opts ...sql.OrderTermOption) OrderOption {
	return sql.OrderByField(FieldUpdatedAt, opts...).ToFunc()
}
