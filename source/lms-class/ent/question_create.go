// Code generated by ent, DO NOT EDIT.

package ent

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	entquestion "lms-class/ent/question"
	"time"

	"entgo.io/ent/dialect/sql/sqlgraph"
	"entgo.io/ent/schema/field"
)

// QuestionCreate is the builder for creating a Question entity.
type QuestionCreate struct {
	config
	mutation *QuestionMutation
	hooks    []Hook
}

// SetContext sets the "context" field.
func (qc *QuestionCreate) SetContext(s string) *QuestionCreate {
	qc.mutation.SetContext(s)
	return qc
}

// SetContextId sets the "contextId" field.
func (qc *QuestionCreate) SetContextId(i int) *QuestionCreate {
	qc.mutation.SetContextId(i)
	return qc
}

// SetPosition sets the "position" field.
func (qc *QuestionCreate) SetPosition(i int) *QuestionCreate {
	qc.mutation.SetPosition(i)
	return qc
}

// SetQuestionType sets the "questionType" field.
func (qc *QuestionCreate) SetQuestionType(s string) *QuestionCreate {
	qc.mutation.SetQuestionType(s)
	return qc
}

// SetData sets the "data" field.
func (qc *QuestionCreate) SetData(jm json.RawMessage) *QuestionCreate {
	qc.mutation.SetData(jm)
	return qc
}

// SetUpdatedAt sets the "updatedAt" field.
func (qc *QuestionCreate) SetUpdatedAt(t time.Time) *QuestionCreate {
	qc.mutation.SetUpdatedAt(t)
	return qc
}

// SetVersion sets the "version" field.
func (qc *QuestionCreate) SetVersion(i int64) *QuestionCreate {
	qc.mutation.SetVersion(i)
	return qc
}

// SetNillableVersion sets the "version" field if the given value is not nil.
func (qc *QuestionCreate) SetNillableVersion(i *int64) *QuestionCreate {
	if i != nil {
		qc.SetVersion(*i)
	}
	return qc
}

// Mutation returns the QuestionMutation object of the builder.
func (qc *QuestionCreate) Mutation() *QuestionMutation {
	return qc.mutation
}

// Save creates the Question in the database.
func (qc *QuestionCreate) Save(ctx context.Context) (*Question, error) {
	qc.defaults()
	return withHooks(ctx, qc.sqlSave, qc.mutation, qc.hooks)
}

// SaveX calls Save and panics if Save returns an error.
func (qc *QuestionCreate) SaveX(ctx context.Context) *Question {
	v, err := qc.Save(ctx)
	if err != nil {
		panic(err)
	}
	return v
}

// Exec executes the query.
func (qc *QuestionCreate) Exec(ctx context.Context) error {
	_, err := qc.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (qc *QuestionCreate) ExecX(ctx context.Context) {
	if err := qc.Exec(ctx); err != nil {
		panic(err)
	}
}

// defaults sets the default values of the builder before save.
func (qc *QuestionCreate) defaults() {
	if _, ok := qc.mutation.Version(); !ok {
		v := entquestion.DefaultVersion()
		qc.mutation.SetVersion(v)
	}
}

// check runs all checks and user-defined validators on the builder.
func (qc *QuestionCreate) check() error {
	if _, ok := qc.mutation.Context(); !ok {
		return &ValidationError{Name: "context", err: errors.New(`ent: missing required field "Question.context"`)}
	}
	if _, ok := qc.mutation.ContextId(); !ok {
		return &ValidationError{Name: "contextId", err: errors.New(`ent: missing required field "Question.contextId"`)}
	}
	if _, ok := qc.mutation.Position(); !ok {
		return &ValidationError{Name: "position", err: errors.New(`ent: missing required field "Question.position"`)}
	}
	if _, ok := qc.mutation.QuestionType(); !ok {
		return &ValidationError{Name: "questionType", err: errors.New(`ent: missing required field "Question.questionType"`)}
	}
	if _, ok := qc.mutation.Data(); !ok {
		return &ValidationError{Name: "data", err: errors.New(`ent: missing required field "Question.data"`)}
	}
	if _, ok := qc.mutation.UpdatedAt(); !ok {
		return &ValidationError{Name: "updatedAt", err: errors.New(`ent: missing required field "Question.updatedAt"`)}
	}
	if _, ok := qc.mutation.Version(); !ok {
		return &ValidationError{Name: "version", err: errors.New(`ent: missing required field "Question.version"`)}
	}
	return nil
}

func (qc *QuestionCreate) sqlSave(ctx context.Context) (*Question, error) {
	if err := qc.check(); err != nil {
		return nil, err
	}
	_node, _spec := qc.createSpec()
	if err := sqlgraph.CreateNode(ctx, qc.driver, _spec); err != nil {
		if sqlgraph.IsConstraintError(err) {
			err = &ConstraintError{msg: err.Error(), wrap: err}
		}
		return nil, err
	}
	id := _spec.ID.Value.(int64)
	_node.ID = int(id)
	qc.mutation.id = &_node.ID
	qc.mutation.done = true
	return _node, nil
}

func (qc *QuestionCreate) createSpec() (*Question, *sqlgraph.CreateSpec) {
	var (
		_node = &Question{config: qc.config}
		_spec = sqlgraph.NewCreateSpec(entquestion.Table, sqlgraph.NewFieldSpec(entquestion.FieldID, field.TypeInt))
	)
	if value, ok := qc.mutation.Context(); ok {
		_spec.SetField(entquestion.FieldContext, field.TypeString, value)
		_node.Context = value
	}
	if value, ok := qc.mutation.ContextId(); ok {
		_spec.SetField(entquestion.FieldContextId, field.TypeInt, value)
		_node.ContextId = value
	}
	if value, ok := qc.mutation.Position(); ok {
		_spec.SetField(entquestion.FieldPosition, field.TypeInt, value)
		_node.Position = value
	}
	if value, ok := qc.mutation.QuestionType(); ok {
		_spec.SetField(entquestion.FieldQuestionType, field.TypeString, value)
		_node.QuestionType = value
	}
	if value, ok := qc.mutation.Data(); ok {
		_spec.SetField(entquestion.FieldData, field.TypeJSON, value)
		_node.Data = value
	}
	if value, ok := qc.mutation.UpdatedAt(); ok {
		_spec.SetField(entquestion.FieldUpdatedAt, field.TypeTime, value)
		_node.UpdatedAt = value
	}
	if value, ok := qc.mutation.Version(); ok {
		_spec.SetField(entquestion.FieldVersion, field.TypeInt64, value)
		_node.Version = value
	}
	return _node, _spec
}

// QuestionCreateBulk is the builder for creating many Question entities in bulk.
type QuestionCreateBulk struct {
	config
	builders []*QuestionCreate
}

// Save creates the Question entities in the database.
func (qcb *QuestionCreateBulk) Save(ctx context.Context) ([]*Question, error) {
	specs := make([]*sqlgraph.CreateSpec, len(qcb.builders))
	nodes := make([]*Question, len(qcb.builders))
	mutators := make([]Mutator, len(qcb.builders))
	for i := range qcb.builders {
		func(i int, root context.Context) {
			builder := qcb.builders[i]
			builder.defaults()
			var mut Mutator = MutateFunc(func(ctx context.Context, m Mutation) (Value, error) {
				mutation, ok := m.(*QuestionMutation)
				if !ok {
					return nil, fmt.Errorf("unexpected mutation type %T", m)
				}
				if err := builder.check(); err != nil {
					return nil, err
				}
				builder.mutation = mutation
				var err error
				nodes[i], specs[i] = builder.createSpec()
				if i < len(mutators)-1 {
					_, err = mutators[i+1].Mutate(root, qcb.builders[i+1].mutation)
				} else {
					spec := &sqlgraph.BatchCreateSpec{Nodes: specs}
					// Invoke the actual operation on the latest mutation in the chain.
					if err = sqlgraph.BatchCreate(ctx, qcb.driver, spec); err != nil {
						if sqlgraph.IsConstraintError(err) {
							err = &ConstraintError{msg: err.Error(), wrap: err}
						}
					}
				}
				if err != nil {
					return nil, err
				}
				mutation.id = &nodes[i].ID
				if specs[i].ID.Value != nil {
					id := specs[i].ID.Value.(int64)
					nodes[i].ID = int(id)
				}
				mutation.done = true
				return nodes[i], nil
			})
			for i := len(builder.hooks) - 1; i >= 0; i-- {
				mut = builder.hooks[i](mut)
			}
			mutators[i] = mut
		}(i, ctx)
	}
	if len(mutators) > 0 {
		if _, err := mutators[0].Mutate(ctx, qcb.builders[0].mutation); err != nil {
			return nil, err
		}
	}
	return nodes, nil
}

// SaveX is like Save, but panics if an error occurs.
func (qcb *QuestionCreateBulk) SaveX(ctx context.Context) []*Question {
	v, err := qcb.Save(ctx)
	if err != nil {
		panic(err)
	}
	return v
}

// Exec executes the query.
func (qcb *QuestionCreateBulk) Exec(ctx context.Context) error {
	_, err := qcb.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (qcb *QuestionCreateBulk) ExecX(ctx context.Context) {
	if err := qcb.Exec(ctx); err != nil {
		panic(err)
	}
}
