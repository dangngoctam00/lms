// Code generated by ent, DO NOT EDIT.

package ent

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"lms-class/ent/questionhistory"
	"time"

	"entgo.io/ent/dialect/sql/sqlgraph"
	"entgo.io/ent/schema/field"
	"github.com/flume/enthistory"
)

// QuestionHistoryCreate is the builder for creating a QuestionHistory entity.
type QuestionHistoryCreate struct {
	config
	mutation *QuestionHistoryMutation
	hooks    []Hook
}

// SetHistoryTime sets the "history_time" field.
func (qhc *QuestionHistoryCreate) SetHistoryTime(t time.Time) *QuestionHistoryCreate {
	qhc.mutation.SetHistoryTime(t)
	return qhc
}

// SetNillableHistoryTime sets the "history_time" field if the given value is not nil.
func (qhc *QuestionHistoryCreate) SetNillableHistoryTime(t *time.Time) *QuestionHistoryCreate {
	if t != nil {
		qhc.SetHistoryTime(*t)
	}
	return qhc
}

// SetOperation sets the "operation" field.
func (qhc *QuestionHistoryCreate) SetOperation(et enthistory.OpType) *QuestionHistoryCreate {
	qhc.mutation.SetOperation(et)
	return qhc
}

// SetRef sets the "ref" field.
func (qhc *QuestionHistoryCreate) SetRef(i int) *QuestionHistoryCreate {
	qhc.mutation.SetRef(i)
	return qhc
}

// SetNillableRef sets the "ref" field if the given value is not nil.
func (qhc *QuestionHistoryCreate) SetNillableRef(i *int) *QuestionHistoryCreate {
	if i != nil {
		qhc.SetRef(*i)
	}
	return qhc
}

// SetUpdatedBy sets the "updated_by" field.
func (qhc *QuestionHistoryCreate) SetUpdatedBy(i int) *QuestionHistoryCreate {
	qhc.mutation.SetUpdatedBy(i)
	return qhc
}

// SetNillableUpdatedBy sets the "updated_by" field if the given value is not nil.
func (qhc *QuestionHistoryCreate) SetNillableUpdatedBy(i *int) *QuestionHistoryCreate {
	if i != nil {
		qhc.SetUpdatedBy(*i)
	}
	return qhc
}

// SetContext sets the "context" field.
func (qhc *QuestionHistoryCreate) SetContext(s string) *QuestionHistoryCreate {
	qhc.mutation.SetContext(s)
	return qhc
}

// SetContextId sets the "contextId" field.
func (qhc *QuestionHistoryCreate) SetContextId(i int) *QuestionHistoryCreate {
	qhc.mutation.SetContextId(i)
	return qhc
}

// SetPosition sets the "position" field.
func (qhc *QuestionHistoryCreate) SetPosition(i int) *QuestionHistoryCreate {
	qhc.mutation.SetPosition(i)
	return qhc
}

// SetQuestionType sets the "questionType" field.
func (qhc *QuestionHistoryCreate) SetQuestionType(s string) *QuestionHistoryCreate {
	qhc.mutation.SetQuestionType(s)
	return qhc
}

// SetData sets the "data" field.
func (qhc *QuestionHistoryCreate) SetData(jm json.RawMessage) *QuestionHistoryCreate {
	qhc.mutation.SetData(jm)
	return qhc
}

// SetUpdatedAt sets the "updatedAt" field.
func (qhc *QuestionHistoryCreate) SetUpdatedAt(t time.Time) *QuestionHistoryCreate {
	qhc.mutation.SetUpdatedAt(t)
	return qhc
}

// SetVersion sets the "version" field.
func (qhc *QuestionHistoryCreate) SetVersion(i int64) *QuestionHistoryCreate {
	qhc.mutation.SetVersion(i)
	return qhc
}

// SetNillableVersion sets the "version" field if the given value is not nil.
func (qhc *QuestionHistoryCreate) SetNillableVersion(i *int64) *QuestionHistoryCreate {
	if i != nil {
		qhc.SetVersion(*i)
	}
	return qhc
}

// Mutation returns the QuestionHistoryMutation object of the builder.
func (qhc *QuestionHistoryCreate) Mutation() *QuestionHistoryMutation {
	return qhc.mutation
}

// Save creates the QuestionHistory in the database.
func (qhc *QuestionHistoryCreate) Save(ctx context.Context) (*QuestionHistory, error) {
	qhc.defaults()
	return withHooks(ctx, qhc.sqlSave, qhc.mutation, qhc.hooks)
}

// SaveX calls Save and panics if Save returns an error.
func (qhc *QuestionHistoryCreate) SaveX(ctx context.Context) *QuestionHistory {
	v, err := qhc.Save(ctx)
	if err != nil {
		panic(err)
	}
	return v
}

// Exec executes the query.
func (qhc *QuestionHistoryCreate) Exec(ctx context.Context) error {
	_, err := qhc.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (qhc *QuestionHistoryCreate) ExecX(ctx context.Context) {
	if err := qhc.Exec(ctx); err != nil {
		panic(err)
	}
}

// defaults sets the default values of the builder before save.
func (qhc *QuestionHistoryCreate) defaults() {
	if _, ok := qhc.mutation.HistoryTime(); !ok {
		v := questionhistory.DefaultHistoryTime()
		qhc.mutation.SetHistoryTime(v)
	}
	if _, ok := qhc.mutation.Version(); !ok {
		v := questionhistory.DefaultVersion()
		qhc.mutation.SetVersion(v)
	}
}

// check runs all checks and user-defined validators on the builder.
func (qhc *QuestionHistoryCreate) check() error {
	if _, ok := qhc.mutation.HistoryTime(); !ok {
		return &ValidationError{Name: "history_time", err: errors.New(`ent: missing required field "QuestionHistory.history_time"`)}
	}
	if _, ok := qhc.mutation.Operation(); !ok {
		return &ValidationError{Name: "operation", err: errors.New(`ent: missing required field "QuestionHistory.operation"`)}
	}
	if v, ok := qhc.mutation.Operation(); ok {
		if err := questionhistory.OperationValidator(v); err != nil {
			return &ValidationError{Name: "operation", err: fmt.Errorf(`ent: validator failed for field "QuestionHistory.operation": %w`, err)}
		}
	}
	if _, ok := qhc.mutation.Context(); !ok {
		return &ValidationError{Name: "context", err: errors.New(`ent: missing required field "QuestionHistory.context"`)}
	}
	if _, ok := qhc.mutation.ContextId(); !ok {
		return &ValidationError{Name: "contextId", err: errors.New(`ent: missing required field "QuestionHistory.contextId"`)}
	}
	if _, ok := qhc.mutation.Position(); !ok {
		return &ValidationError{Name: "position", err: errors.New(`ent: missing required field "QuestionHistory.position"`)}
	}
	if _, ok := qhc.mutation.QuestionType(); !ok {
		return &ValidationError{Name: "questionType", err: errors.New(`ent: missing required field "QuestionHistory.questionType"`)}
	}
	if _, ok := qhc.mutation.Data(); !ok {
		return &ValidationError{Name: "data", err: errors.New(`ent: missing required field "QuestionHistory.data"`)}
	}
	if _, ok := qhc.mutation.UpdatedAt(); !ok {
		return &ValidationError{Name: "updatedAt", err: errors.New(`ent: missing required field "QuestionHistory.updatedAt"`)}
	}
	if _, ok := qhc.mutation.Version(); !ok {
		return &ValidationError{Name: "version", err: errors.New(`ent: missing required field "QuestionHistory.version"`)}
	}
	return nil
}

func (qhc *QuestionHistoryCreate) sqlSave(ctx context.Context) (*QuestionHistory, error) {
	if err := qhc.check(); err != nil {
		return nil, err
	}
	_node, _spec := qhc.createSpec()
	if err := sqlgraph.CreateNode(ctx, qhc.driver, _spec); err != nil {
		if sqlgraph.IsConstraintError(err) {
			err = &ConstraintError{msg: err.Error(), wrap: err}
		}
		return nil, err
	}
	id := _spec.ID.Value.(int64)
	_node.ID = int(id)
	qhc.mutation.id = &_node.ID
	qhc.mutation.done = true
	return _node, nil
}

func (qhc *QuestionHistoryCreate) createSpec() (*QuestionHistory, *sqlgraph.CreateSpec) {
	var (
		_node = &QuestionHistory{config: qhc.config}
		_spec = sqlgraph.NewCreateSpec(questionhistory.Table, sqlgraph.NewFieldSpec(questionhistory.FieldID, field.TypeInt))
	)
	if value, ok := qhc.mutation.HistoryTime(); ok {
		_spec.SetField(questionhistory.FieldHistoryTime, field.TypeTime, value)
		_node.HistoryTime = value
	}
	if value, ok := qhc.mutation.Operation(); ok {
		_spec.SetField(questionhistory.FieldOperation, field.TypeEnum, value)
		_node.Operation = value
	}
	if value, ok := qhc.mutation.Ref(); ok {
		_spec.SetField(questionhistory.FieldRef, field.TypeInt, value)
		_node.Ref = value
	}
	if value, ok := qhc.mutation.UpdatedBy(); ok {
		_spec.SetField(questionhistory.FieldUpdatedBy, field.TypeInt, value)
		_node.UpdatedBy = &value
	}
	if value, ok := qhc.mutation.Context(); ok {
		_spec.SetField(questionhistory.FieldContext, field.TypeString, value)
		_node.Context = value
	}
	if value, ok := qhc.mutation.ContextId(); ok {
		_spec.SetField(questionhistory.FieldContextId, field.TypeInt, value)
		_node.ContextId = value
	}
	if value, ok := qhc.mutation.Position(); ok {
		_spec.SetField(questionhistory.FieldPosition, field.TypeInt, value)
		_node.Position = value
	}
	if value, ok := qhc.mutation.QuestionType(); ok {
		_spec.SetField(questionhistory.FieldQuestionType, field.TypeString, value)
		_node.QuestionType = value
	}
	if value, ok := qhc.mutation.Data(); ok {
		_spec.SetField(questionhistory.FieldData, field.TypeJSON, value)
		_node.Data = value
	}
	if value, ok := qhc.mutation.UpdatedAt(); ok {
		_spec.SetField(questionhistory.FieldUpdatedAt, field.TypeTime, value)
		_node.UpdatedAt = value
	}
	if value, ok := qhc.mutation.Version(); ok {
		_spec.SetField(questionhistory.FieldVersion, field.TypeInt64, value)
		_node.Version = value
	}
	return _node, _spec
}

// QuestionHistoryCreateBulk is the builder for creating many QuestionHistory entities in bulk.
type QuestionHistoryCreateBulk struct {
	config
	builders []*QuestionHistoryCreate
}

// Save creates the QuestionHistory entities in the database.
func (qhcb *QuestionHistoryCreateBulk) Save(ctx context.Context) ([]*QuestionHistory, error) {
	specs := make([]*sqlgraph.CreateSpec, len(qhcb.builders))
	nodes := make([]*QuestionHistory, len(qhcb.builders))
	mutators := make([]Mutator, len(qhcb.builders))
	for i := range qhcb.builders {
		func(i int, root context.Context) {
			builder := qhcb.builders[i]
			builder.defaults()
			var mut Mutator = MutateFunc(func(ctx context.Context, m Mutation) (Value, error) {
				mutation, ok := m.(*QuestionHistoryMutation)
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
					_, err = mutators[i+1].Mutate(root, qhcb.builders[i+1].mutation)
				} else {
					spec := &sqlgraph.BatchCreateSpec{Nodes: specs}
					// Invoke the actual operation on the latest mutation in the chain.
					if err = sqlgraph.BatchCreate(ctx, qhcb.driver, spec); err != nil {
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
		if _, err := mutators[0].Mutate(ctx, qhcb.builders[0].mutation); err != nil {
			return nil, err
		}
	}
	return nodes, nil
}

// SaveX is like Save, but panics if an error occurs.
func (qhcb *QuestionHistoryCreateBulk) SaveX(ctx context.Context) []*QuestionHistory {
	v, err := qhcb.Save(ctx)
	if err != nil {
		panic(err)
	}
	return v
}

// Exec executes the query.
func (qhcb *QuestionHistoryCreateBulk) Exec(ctx context.Context) error {
	_, err := qhcb.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (qhcb *QuestionHistoryCreateBulk) ExecX(ctx context.Context) {
	if err := qhcb.Exec(ctx); err != nil {
		panic(err)
	}
}
