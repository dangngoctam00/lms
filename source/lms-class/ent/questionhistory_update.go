// Code generated by ent, DO NOT EDIT.

package ent

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"lms-class/ent/predicate"
	"lms-class/ent/questionhistory"
	"time"

	"entgo.io/ent/dialect/sql"
	"entgo.io/ent/dialect/sql/sqlgraph"
	"entgo.io/ent/dialect/sql/sqljson"
	"entgo.io/ent/schema/field"
)

// QuestionHistoryUpdate is the builder for updating QuestionHistory entities.
type QuestionHistoryUpdate struct {
	config
	hooks    []Hook
	mutation *QuestionHistoryMutation
}

// Where appends a list predicates to the QuestionHistoryUpdate builder.
func (qhu *QuestionHistoryUpdate) Where(ps ...predicate.QuestionHistory) *QuestionHistoryUpdate {
	qhu.mutation.Where(ps...)
	return qhu
}

// SetContext sets the "context" field.
func (qhu *QuestionHistoryUpdate) SetContext(s string) *QuestionHistoryUpdate {
	qhu.mutation.SetContext(s)
	return qhu
}

// SetContextId sets the "contextId" field.
func (qhu *QuestionHistoryUpdate) SetContextId(i int) *QuestionHistoryUpdate {
	qhu.mutation.ResetContextId()
	qhu.mutation.SetContextId(i)
	return qhu
}

// AddContextId adds i to the "contextId" field.
func (qhu *QuestionHistoryUpdate) AddContextId(i int) *QuestionHistoryUpdate {
	qhu.mutation.AddContextId(i)
	return qhu
}

// SetPosition sets the "position" field.
func (qhu *QuestionHistoryUpdate) SetPosition(i int) *QuestionHistoryUpdate {
	qhu.mutation.ResetPosition()
	qhu.mutation.SetPosition(i)
	return qhu
}

// AddPosition adds i to the "position" field.
func (qhu *QuestionHistoryUpdate) AddPosition(i int) *QuestionHistoryUpdate {
	qhu.mutation.AddPosition(i)
	return qhu
}

// SetQuestionType sets the "questionType" field.
func (qhu *QuestionHistoryUpdate) SetQuestionType(s string) *QuestionHistoryUpdate {
	qhu.mutation.SetQuestionType(s)
	return qhu
}

// SetData sets the "data" field.
func (qhu *QuestionHistoryUpdate) SetData(jm json.RawMessage) *QuestionHistoryUpdate {
	qhu.mutation.SetData(jm)
	return qhu
}

// AppendData appends jm to the "data" field.
func (qhu *QuestionHistoryUpdate) AppendData(jm json.RawMessage) *QuestionHistoryUpdate {
	qhu.mutation.AppendData(jm)
	return qhu
}

// SetUpdatedAt sets the "updatedAt" field.
func (qhu *QuestionHistoryUpdate) SetUpdatedAt(t time.Time) *QuestionHistoryUpdate {
	qhu.mutation.SetUpdatedAt(t)
	return qhu
}

// Mutation returns the QuestionHistoryMutation object of the builder.
func (qhu *QuestionHistoryUpdate) Mutation() *QuestionHistoryMutation {
	return qhu.mutation
}

// Save executes the query and returns the number of nodes affected by the update operation.
func (qhu *QuestionHistoryUpdate) Save(ctx context.Context) (int, error) {
	return withHooks(ctx, qhu.sqlSave, qhu.mutation, qhu.hooks)
}

// SaveX is like Save, but panics if an error occurs.
func (qhu *QuestionHistoryUpdate) SaveX(ctx context.Context) int {
	affected, err := qhu.Save(ctx)
	if err != nil {
		panic(err)
	}
	return affected
}

// Exec executes the query.
func (qhu *QuestionHistoryUpdate) Exec(ctx context.Context) error {
	_, err := qhu.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (qhu *QuestionHistoryUpdate) ExecX(ctx context.Context) {
	if err := qhu.Exec(ctx); err != nil {
		panic(err)
	}
}

func (qhu *QuestionHistoryUpdate) sqlSave(ctx context.Context) (n int, err error) {
	_spec := sqlgraph.NewUpdateSpec(questionhistory.Table, questionhistory.Columns, sqlgraph.NewFieldSpec(questionhistory.FieldID, field.TypeInt))
	if ps := qhu.mutation.predicates; len(ps) > 0 {
		_spec.Predicate = func(selector *sql.Selector) {
			for i := range ps {
				ps[i](selector)
			}
		}
	}
	if qhu.mutation.RefCleared() {
		_spec.ClearField(questionhistory.FieldRef, field.TypeInt)
	}
	if qhu.mutation.UpdatedByCleared() {
		_spec.ClearField(questionhistory.FieldUpdatedBy, field.TypeInt)
	}
	if value, ok := qhu.mutation.Context(); ok {
		_spec.SetField(questionhistory.FieldContext, field.TypeString, value)
	}
	if value, ok := qhu.mutation.ContextId(); ok {
		_spec.SetField(questionhistory.FieldContextId, field.TypeInt, value)
	}
	if value, ok := qhu.mutation.AddedContextId(); ok {
		_spec.AddField(questionhistory.FieldContextId, field.TypeInt, value)
	}
	if value, ok := qhu.mutation.Position(); ok {
		_spec.SetField(questionhistory.FieldPosition, field.TypeInt, value)
	}
	if value, ok := qhu.mutation.AddedPosition(); ok {
		_spec.AddField(questionhistory.FieldPosition, field.TypeInt, value)
	}
	if value, ok := qhu.mutation.QuestionType(); ok {
		_spec.SetField(questionhistory.FieldQuestionType, field.TypeString, value)
	}
	if value, ok := qhu.mutation.Data(); ok {
		_spec.SetField(questionhistory.FieldData, field.TypeJSON, value)
	}
	if value, ok := qhu.mutation.AppendedData(); ok {
		_spec.AddModifier(func(u *sql.UpdateBuilder) {
			sqljson.Append(u, questionhistory.FieldData, value)
		})
	}
	if value, ok := qhu.mutation.UpdatedAt(); ok {
		_spec.SetField(questionhistory.FieldUpdatedAt, field.TypeTime, value)
	}
	if n, err = sqlgraph.UpdateNodes(ctx, qhu.driver, _spec); err != nil {
		if _, ok := err.(*sqlgraph.NotFoundError); ok {
			err = &NotFoundError{questionhistory.Label}
		} else if sqlgraph.IsConstraintError(err) {
			err = &ConstraintError{msg: err.Error(), wrap: err}
		}
		return 0, err
	}
	qhu.mutation.done = true
	return n, nil
}

// QuestionHistoryUpdateOne is the builder for updating a single QuestionHistory entity.
type QuestionHistoryUpdateOne struct {
	config
	fields   []string
	hooks    []Hook
	mutation *QuestionHistoryMutation
}

// SetContext sets the "context" field.
func (qhuo *QuestionHistoryUpdateOne) SetContext(s string) *QuestionHistoryUpdateOne {
	qhuo.mutation.SetContext(s)
	return qhuo
}

// SetContextId sets the "contextId" field.
func (qhuo *QuestionHistoryUpdateOne) SetContextId(i int) *QuestionHistoryUpdateOne {
	qhuo.mutation.ResetContextId()
	qhuo.mutation.SetContextId(i)
	return qhuo
}

// AddContextId adds i to the "contextId" field.
func (qhuo *QuestionHistoryUpdateOne) AddContextId(i int) *QuestionHistoryUpdateOne {
	qhuo.mutation.AddContextId(i)
	return qhuo
}

// SetPosition sets the "position" field.
func (qhuo *QuestionHistoryUpdateOne) SetPosition(i int) *QuestionHistoryUpdateOne {
	qhuo.mutation.ResetPosition()
	qhuo.mutation.SetPosition(i)
	return qhuo
}

// AddPosition adds i to the "position" field.
func (qhuo *QuestionHistoryUpdateOne) AddPosition(i int) *QuestionHistoryUpdateOne {
	qhuo.mutation.AddPosition(i)
	return qhuo
}

// SetQuestionType sets the "questionType" field.
func (qhuo *QuestionHistoryUpdateOne) SetQuestionType(s string) *QuestionHistoryUpdateOne {
	qhuo.mutation.SetQuestionType(s)
	return qhuo
}

// SetData sets the "data" field.
func (qhuo *QuestionHistoryUpdateOne) SetData(jm json.RawMessage) *QuestionHistoryUpdateOne {
	qhuo.mutation.SetData(jm)
	return qhuo
}

// AppendData appends jm to the "data" field.
func (qhuo *QuestionHistoryUpdateOne) AppendData(jm json.RawMessage) *QuestionHistoryUpdateOne {
	qhuo.mutation.AppendData(jm)
	return qhuo
}

// SetUpdatedAt sets the "updatedAt" field.
func (qhuo *QuestionHistoryUpdateOne) SetUpdatedAt(t time.Time) *QuestionHistoryUpdateOne {
	qhuo.mutation.SetUpdatedAt(t)
	return qhuo
}

// Mutation returns the QuestionHistoryMutation object of the builder.
func (qhuo *QuestionHistoryUpdateOne) Mutation() *QuestionHistoryMutation {
	return qhuo.mutation
}

// Where appends a list predicates to the QuestionHistoryUpdate builder.
func (qhuo *QuestionHistoryUpdateOne) Where(ps ...predicate.QuestionHistory) *QuestionHistoryUpdateOne {
	qhuo.mutation.Where(ps...)
	return qhuo
}

// Select allows selecting one or more fields (columns) of the returned entity.
// The default is selecting all fields defined in the entity schema.
func (qhuo *QuestionHistoryUpdateOne) Select(field string, fields ...string) *QuestionHistoryUpdateOne {
	qhuo.fields = append([]string{field}, fields...)
	return qhuo
}

// Save executes the query and returns the updated QuestionHistory entity.
func (qhuo *QuestionHistoryUpdateOne) Save(ctx context.Context) (*QuestionHistory, error) {
	return withHooks(ctx, qhuo.sqlSave, qhuo.mutation, qhuo.hooks)
}

// SaveX is like Save, but panics if an error occurs.
func (qhuo *QuestionHistoryUpdateOne) SaveX(ctx context.Context) *QuestionHistory {
	node, err := qhuo.Save(ctx)
	if err != nil {
		panic(err)
	}
	return node
}

// Exec executes the query on the entity.
func (qhuo *QuestionHistoryUpdateOne) Exec(ctx context.Context) error {
	_, err := qhuo.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (qhuo *QuestionHistoryUpdateOne) ExecX(ctx context.Context) {
	if err := qhuo.Exec(ctx); err != nil {
		panic(err)
	}
}

func (qhuo *QuestionHistoryUpdateOne) sqlSave(ctx context.Context) (_node *QuestionHistory, err error) {
	_spec := sqlgraph.NewUpdateSpec(questionhistory.Table, questionhistory.Columns, sqlgraph.NewFieldSpec(questionhistory.FieldID, field.TypeInt))
	id, ok := qhuo.mutation.ID()
	if !ok {
		return nil, &ValidationError{Name: "id", err: errors.New(`ent: missing "QuestionHistory.id" for update`)}
	}
	_spec.Node.ID.Value = id
	if fields := qhuo.fields; len(fields) > 0 {
		_spec.Node.Columns = make([]string, 0, len(fields))
		_spec.Node.Columns = append(_spec.Node.Columns, questionhistory.FieldID)
		for _, f := range fields {
			if !questionhistory.ValidColumn(f) {
				return nil, &ValidationError{Name: f, err: fmt.Errorf("ent: invalid field %q for query", f)}
			}
			if f != questionhistory.FieldID {
				_spec.Node.Columns = append(_spec.Node.Columns, f)
			}
		}
	}
	if ps := qhuo.mutation.predicates; len(ps) > 0 {
		_spec.Predicate = func(selector *sql.Selector) {
			for i := range ps {
				ps[i](selector)
			}
		}
	}
	if qhuo.mutation.RefCleared() {
		_spec.ClearField(questionhistory.FieldRef, field.TypeInt)
	}
	if qhuo.mutation.UpdatedByCleared() {
		_spec.ClearField(questionhistory.FieldUpdatedBy, field.TypeInt)
	}
	if value, ok := qhuo.mutation.Context(); ok {
		_spec.SetField(questionhistory.FieldContext, field.TypeString, value)
	}
	if value, ok := qhuo.mutation.ContextId(); ok {
		_spec.SetField(questionhistory.FieldContextId, field.TypeInt, value)
	}
	if value, ok := qhuo.mutation.AddedContextId(); ok {
		_spec.AddField(questionhistory.FieldContextId, field.TypeInt, value)
	}
	if value, ok := qhuo.mutation.Position(); ok {
		_spec.SetField(questionhistory.FieldPosition, field.TypeInt, value)
	}
	if value, ok := qhuo.mutation.AddedPosition(); ok {
		_spec.AddField(questionhistory.FieldPosition, field.TypeInt, value)
	}
	if value, ok := qhuo.mutation.QuestionType(); ok {
		_spec.SetField(questionhistory.FieldQuestionType, field.TypeString, value)
	}
	if value, ok := qhuo.mutation.Data(); ok {
		_spec.SetField(questionhistory.FieldData, field.TypeJSON, value)
	}
	if value, ok := qhuo.mutation.AppendedData(); ok {
		_spec.AddModifier(func(u *sql.UpdateBuilder) {
			sqljson.Append(u, questionhistory.FieldData, value)
		})
	}
	if value, ok := qhuo.mutation.UpdatedAt(); ok {
		_spec.SetField(questionhistory.FieldUpdatedAt, field.TypeTime, value)
	}
	_node = &QuestionHistory{config: qhuo.config}
	_spec.Assign = _node.assignValues
	_spec.ScanValues = _node.scanValues
	if err = sqlgraph.UpdateNode(ctx, qhuo.driver, _spec); err != nil {
		if _, ok := err.(*sqlgraph.NotFoundError); ok {
			err = &NotFoundError{questionhistory.Label}
		} else if sqlgraph.IsConstraintError(err) {
			err = &ConstraintError{msg: err.Error(), wrap: err}
		}
		return nil, err
	}
	qhuo.mutation.done = true
	return _node, nil
}