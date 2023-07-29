// Code generated by ent, DO NOT EDIT.

package ent

import (
	"context"
	"errors"
	"fmt"
	"lms-class/ent/exam"
	"lms-class/ent/predicate"
	"time"

	"entgo.io/ent/dialect/sql"
	"entgo.io/ent/dialect/sql/sqlgraph"
	"entgo.io/ent/schema/field"
)

// ExamUpdate is the builder for updating Exam entities.
type ExamUpdate struct {
	config
	hooks    []Hook
	mutation *ExamMutation
}

// Where appends a list predicates to the ExamUpdate builder.
func (eu *ExamUpdate) Where(ps ...predicate.Exam) *ExamUpdate {
	eu.mutation.Where(ps...)
	return eu
}

// SetTitle sets the "title" field.
func (eu *ExamUpdate) SetTitle(s string) *ExamUpdate {
	eu.mutation.SetTitle(s)
	return eu
}

// SetContext sets the "context" field.
func (eu *ExamUpdate) SetContext(s string) *ExamUpdate {
	eu.mutation.SetContext(s)
	return eu
}

// SetContextId sets the "contextId" field.
func (eu *ExamUpdate) SetContextId(s string) *ExamUpdate {
	eu.mutation.SetContextId(s)
	return eu
}

// SetIsPublished sets the "isPublished" field.
func (eu *ExamUpdate) SetIsPublished(b bool) *ExamUpdate {
	eu.mutation.SetIsPublished(b)
	return eu
}

// SetUpdatedAt sets the "updatedAt" field.
func (eu *ExamUpdate) SetUpdatedAt(t time.Time) *ExamUpdate {
	eu.mutation.SetUpdatedAt(t)
	return eu
}

// Mutation returns the ExamMutation object of the builder.
func (eu *ExamUpdate) Mutation() *ExamMutation {
	return eu.mutation
}

// Save executes the query and returns the number of nodes affected by the update operation.
func (eu *ExamUpdate) Save(ctx context.Context) (int, error) {
	return withHooks(ctx, eu.sqlSave, eu.mutation, eu.hooks)
}

// SaveX is like Save, but panics if an error occurs.
func (eu *ExamUpdate) SaveX(ctx context.Context) int {
	affected, err := eu.Save(ctx)
	if err != nil {
		panic(err)
	}
	return affected
}

// Exec executes the query.
func (eu *ExamUpdate) Exec(ctx context.Context) error {
	_, err := eu.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (eu *ExamUpdate) ExecX(ctx context.Context) {
	if err := eu.Exec(ctx); err != nil {
		panic(err)
	}
}

func (eu *ExamUpdate) sqlSave(ctx context.Context) (n int, err error) {
	_spec := sqlgraph.NewUpdateSpec(exam.Table, exam.Columns, sqlgraph.NewFieldSpec(exam.FieldID, field.TypeInt))
	if ps := eu.mutation.predicates; len(ps) > 0 {
		_spec.Predicate = func(selector *sql.Selector) {
			for i := range ps {
				ps[i](selector)
			}
		}
	}
	if value, ok := eu.mutation.Title(); ok {
		_spec.SetField(exam.FieldTitle, field.TypeString, value)
	}
	if value, ok := eu.mutation.Context(); ok {
		_spec.SetField(exam.FieldContext, field.TypeString, value)
	}
	if value, ok := eu.mutation.ContextId(); ok {
		_spec.SetField(exam.FieldContextId, field.TypeString, value)
	}
	if value, ok := eu.mutation.IsPublished(); ok {
		_spec.SetField(exam.FieldIsPublished, field.TypeBool, value)
	}
	if value, ok := eu.mutation.UpdatedAt(); ok {
		_spec.SetField(exam.FieldUpdatedAt, field.TypeTime, value)
	}
	if n, err = sqlgraph.UpdateNodes(ctx, eu.driver, _spec); err != nil {
		if _, ok := err.(*sqlgraph.NotFoundError); ok {
			err = &NotFoundError{exam.Label}
		} else if sqlgraph.IsConstraintError(err) {
			err = &ConstraintError{msg: err.Error(), wrap: err}
		}
		return 0, err
	}
	eu.mutation.done = true
	return n, nil
}

// ExamUpdateOne is the builder for updating a single Exam entity.
type ExamUpdateOne struct {
	config
	fields   []string
	hooks    []Hook
	mutation *ExamMutation
}

// SetTitle sets the "title" field.
func (euo *ExamUpdateOne) SetTitle(s string) *ExamUpdateOne {
	euo.mutation.SetTitle(s)
	return euo
}

// SetContext sets the "context" field.
func (euo *ExamUpdateOne) SetContext(s string) *ExamUpdateOne {
	euo.mutation.SetContext(s)
	return euo
}

// SetContextId sets the "contextId" field.
func (euo *ExamUpdateOne) SetContextId(s string) *ExamUpdateOne {
	euo.mutation.SetContextId(s)
	return euo
}

// SetIsPublished sets the "isPublished" field.
func (euo *ExamUpdateOne) SetIsPublished(b bool) *ExamUpdateOne {
	euo.mutation.SetIsPublished(b)
	return euo
}

// SetUpdatedAt sets the "updatedAt" field.
func (euo *ExamUpdateOne) SetUpdatedAt(t time.Time) *ExamUpdateOne {
	euo.mutation.SetUpdatedAt(t)
	return euo
}

// Mutation returns the ExamMutation object of the builder.
func (euo *ExamUpdateOne) Mutation() *ExamMutation {
	return euo.mutation
}

// Where appends a list predicates to the ExamUpdate builder.
func (euo *ExamUpdateOne) Where(ps ...predicate.Exam) *ExamUpdateOne {
	euo.mutation.Where(ps...)
	return euo
}

// Select allows selecting one or more fields (columns) of the returned entity.
// The default is selecting all fields defined in the entity schema.
func (euo *ExamUpdateOne) Select(field string, fields ...string) *ExamUpdateOne {
	euo.fields = append([]string{field}, fields...)
	return euo
}

// Save executes the query and returns the updated Exam entity.
func (euo *ExamUpdateOne) Save(ctx context.Context) (*Exam, error) {
	return withHooks(ctx, euo.sqlSave, euo.mutation, euo.hooks)
}

// SaveX is like Save, but panics if an error occurs.
func (euo *ExamUpdateOne) SaveX(ctx context.Context) *Exam {
	node, err := euo.Save(ctx)
	if err != nil {
		panic(err)
	}
	return node
}

// Exec executes the query on the entity.
func (euo *ExamUpdateOne) Exec(ctx context.Context) error {
	_, err := euo.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (euo *ExamUpdateOne) ExecX(ctx context.Context) {
	if err := euo.Exec(ctx); err != nil {
		panic(err)
	}
}

func (euo *ExamUpdateOne) sqlSave(ctx context.Context) (_node *Exam, err error) {
	_spec := sqlgraph.NewUpdateSpec(exam.Table, exam.Columns, sqlgraph.NewFieldSpec(exam.FieldID, field.TypeInt))
	id, ok := euo.mutation.ID()
	if !ok {
		return nil, &ValidationError{Name: "id", err: errors.New(`ent: missing "Exam.id" for update`)}
	}
	_spec.Node.ID.Value = id
	if fields := euo.fields; len(fields) > 0 {
		_spec.Node.Columns = make([]string, 0, len(fields))
		_spec.Node.Columns = append(_spec.Node.Columns, exam.FieldID)
		for _, f := range fields {
			if !exam.ValidColumn(f) {
				return nil, &ValidationError{Name: f, err: fmt.Errorf("ent: invalid field %q for query", f)}
			}
			if f != exam.FieldID {
				_spec.Node.Columns = append(_spec.Node.Columns, f)
			}
		}
	}
	if ps := euo.mutation.predicates; len(ps) > 0 {
		_spec.Predicate = func(selector *sql.Selector) {
			for i := range ps {
				ps[i](selector)
			}
		}
	}
	if value, ok := euo.mutation.Title(); ok {
		_spec.SetField(exam.FieldTitle, field.TypeString, value)
	}
	if value, ok := euo.mutation.Context(); ok {
		_spec.SetField(exam.FieldContext, field.TypeString, value)
	}
	if value, ok := euo.mutation.ContextId(); ok {
		_spec.SetField(exam.FieldContextId, field.TypeString, value)
	}
	if value, ok := euo.mutation.IsPublished(); ok {
		_spec.SetField(exam.FieldIsPublished, field.TypeBool, value)
	}
	if value, ok := euo.mutation.UpdatedAt(); ok {
		_spec.SetField(exam.FieldUpdatedAt, field.TypeTime, value)
	}
	_node = &Exam{config: euo.config}
	_spec.Assign = _node.assignValues
	_spec.ScanValues = _node.scanValues
	if err = sqlgraph.UpdateNode(ctx, euo.driver, _spec); err != nil {
		if _, ok := err.(*sqlgraph.NotFoundError); ok {
			err = &NotFoundError{exam.Label}
		} else if sqlgraph.IsConstraintError(err) {
			err = &ConstraintError{msg: err.Error(), wrap: err}
		}
		return nil, err
	}
	euo.mutation.done = true
	return _node, nil
}