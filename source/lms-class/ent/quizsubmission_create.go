// Code generated by ent, DO NOT EDIT.

package ent

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"lms-class/ent/quiz"
	"lms-class/ent/quizsubmission"
	"lms-class/internal/web/dto"
	"time"

	"entgo.io/ent/dialect/sql/sqlgraph"
	"entgo.io/ent/schema/field"
)

// QuizSubmissionCreate is the builder for creating a QuizSubmission entity.
type QuizSubmissionCreate struct {
	config
	mutation *QuizSubmissionMutation
	hooks    []Hook
}

// SetQuizId sets the "quizId" field.
func (qsc *QuizSubmissionCreate) SetQuizId(i int) *QuizSubmissionCreate {
	qsc.mutation.SetQuizId(i)
	return qsc
}

// SetUserId sets the "userId" field.
func (qsc *QuizSubmissionCreate) SetUserId(i int) *QuizSubmissionCreate {
	qsc.mutation.SetUserId(i)
	return qsc
}

// SetStartedAt sets the "startedAt" field.
func (qsc *QuizSubmissionCreate) SetStartedAt(t time.Time) *QuizSubmissionCreate {
	qsc.mutation.SetStartedAt(t)
	return qsc
}

// SetSubmittedAt sets the "submittedAt" field.
func (qsc *QuizSubmissionCreate) SetSubmittedAt(t time.Time) *QuizSubmissionCreate {
	qsc.mutation.SetSubmittedAt(t)
	return qsc
}

// SetNillableSubmittedAt sets the "submittedAt" field if the given value is not nil.
func (qsc *QuizSubmissionCreate) SetNillableSubmittedAt(t *time.Time) *QuizSubmissionCreate {
	if t != nil {
		qsc.SetSubmittedAt(*t)
	}
	return qsc
}

// SetQuestions sets the "questions" field.
func (qsc *QuizSubmissionCreate) SetQuestions(jm json.RawMessage) *QuizSubmissionCreate {
	qsc.mutation.SetQuestions(jm)
	return qsc
}

// SetAnswers sets the "answers" field.
func (qsc *QuizSubmissionCreate) SetAnswers(m map[int][]dto.Key) *QuizSubmissionCreate {
	qsc.mutation.SetAnswers(m)
	return qsc
}

// SetScore sets the "score" field.
func (qsc *QuizSubmissionCreate) SetScore(i int) *QuizSubmissionCreate {
	qsc.mutation.SetScore(i)
	return qsc
}

// SetNillableScore sets the "score" field if the given value is not nil.
func (qsc *QuizSubmissionCreate) SetNillableScore(i *int) *QuizSubmissionCreate {
	if i != nil {
		qsc.SetScore(*i)
	}
	return qsc
}

// SetQuizID sets the "quiz" edge to the Quiz entity by ID.
func (qsc *QuizSubmissionCreate) SetQuizID(id int) *QuizSubmissionCreate {
	qsc.mutation.SetQuizID(id)
	return qsc
}

// SetQuiz sets the "quiz" edge to the Quiz entity.
func (qsc *QuizSubmissionCreate) SetQuiz(q *Quiz) *QuizSubmissionCreate {
	return qsc.SetQuizID(q.ID)
}

// Mutation returns the QuizSubmissionMutation object of the builder.
func (qsc *QuizSubmissionCreate) Mutation() *QuizSubmissionMutation {
	return qsc.mutation
}

// Save creates the QuizSubmission in the database.
func (qsc *QuizSubmissionCreate) Save(ctx context.Context) (*QuizSubmission, error) {
	return withHooks(ctx, qsc.sqlSave, qsc.mutation, qsc.hooks)
}

// SaveX calls Save and panics if Save returns an error.
func (qsc *QuizSubmissionCreate) SaveX(ctx context.Context) *QuizSubmission {
	v, err := qsc.Save(ctx)
	if err != nil {
		panic(err)
	}
	return v
}

// Exec executes the query.
func (qsc *QuizSubmissionCreate) Exec(ctx context.Context) error {
	_, err := qsc.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (qsc *QuizSubmissionCreate) ExecX(ctx context.Context) {
	if err := qsc.Exec(ctx); err != nil {
		panic(err)
	}
}

// check runs all checks and user-defined validators on the builder.
func (qsc *QuizSubmissionCreate) check() error {
	if _, ok := qsc.mutation.QuizId(); !ok {
		return &ValidationError{Name: "quizId", err: errors.New(`ent: missing required field "QuizSubmission.quizId"`)}
	}
	if _, ok := qsc.mutation.UserId(); !ok {
		return &ValidationError{Name: "userId", err: errors.New(`ent: missing required field "QuizSubmission.userId"`)}
	}
	if _, ok := qsc.mutation.StartedAt(); !ok {
		return &ValidationError{Name: "startedAt", err: errors.New(`ent: missing required field "QuizSubmission.startedAt"`)}
	}
	if _, ok := qsc.mutation.Questions(); !ok {
		return &ValidationError{Name: "questions", err: errors.New(`ent: missing required field "QuizSubmission.questions"`)}
	}
	if _, ok := qsc.mutation.QuizID(); !ok {
		return &ValidationError{Name: "quiz", err: errors.New(`ent: missing required edge "QuizSubmission.quiz"`)}
	}
	return nil
}

func (qsc *QuizSubmissionCreate) sqlSave(ctx context.Context) (*QuizSubmission, error) {
	if err := qsc.check(); err != nil {
		return nil, err
	}
	_node, _spec := qsc.createSpec()
	if err := sqlgraph.CreateNode(ctx, qsc.driver, _spec); err != nil {
		if sqlgraph.IsConstraintError(err) {
			err = &ConstraintError{msg: err.Error(), wrap: err}
		}
		return nil, err
	}
	id := _spec.ID.Value.(int64)
	_node.ID = int(id)
	qsc.mutation.id = &_node.ID
	qsc.mutation.done = true
	return _node, nil
}

func (qsc *QuizSubmissionCreate) createSpec() (*QuizSubmission, *sqlgraph.CreateSpec) {
	var (
		_node = &QuizSubmission{config: qsc.config}
		_spec = sqlgraph.NewCreateSpec(quizsubmission.Table, sqlgraph.NewFieldSpec(quizsubmission.FieldID, field.TypeInt))
	)
	if value, ok := qsc.mutation.UserId(); ok {
		_spec.SetField(quizsubmission.FieldUserId, field.TypeInt, value)
		_node.UserId = value
	}
	if value, ok := qsc.mutation.StartedAt(); ok {
		_spec.SetField(quizsubmission.FieldStartedAt, field.TypeTime, value)
		_node.StartedAt = value
	}
	if value, ok := qsc.mutation.SubmittedAt(); ok {
		_spec.SetField(quizsubmission.FieldSubmittedAt, field.TypeTime, value)
		_node.SubmittedAt = &value
	}
	if value, ok := qsc.mutation.Questions(); ok {
		_spec.SetField(quizsubmission.FieldQuestions, field.TypeJSON, value)
		_node.Questions = value
	}
	if value, ok := qsc.mutation.Answers(); ok {
		_spec.SetField(quizsubmission.FieldAnswers, field.TypeJSON, value)
		_node.Answers = value
	}
	if value, ok := qsc.mutation.Score(); ok {
		_spec.SetField(quizsubmission.FieldScore, field.TypeInt, value)
		_node.Score = &value
	}
	if nodes := qsc.mutation.QuizIDs(); len(nodes) > 0 {
		edge := &sqlgraph.EdgeSpec{
			Rel:     sqlgraph.M2O,
			Inverse: true,
			Table:   quizsubmission.QuizTable,
			Columns: []string{quizsubmission.QuizColumn},
			Bidi:    false,
			Target: &sqlgraph.EdgeTarget{
				IDSpec: sqlgraph.NewFieldSpec(quiz.FieldID, field.TypeInt),
			},
		}
		for _, k := range nodes {
			edge.Target.Nodes = append(edge.Target.Nodes, k)
		}
		_node.QuizId = nodes[0]
		_spec.Edges = append(_spec.Edges, edge)
	}
	return _node, _spec
}

// QuizSubmissionCreateBulk is the builder for creating many QuizSubmission entities in bulk.
type QuizSubmissionCreateBulk struct {
	config
	builders []*QuizSubmissionCreate
}

// Save creates the QuizSubmission entities in the database.
func (qscb *QuizSubmissionCreateBulk) Save(ctx context.Context) ([]*QuizSubmission, error) {
	specs := make([]*sqlgraph.CreateSpec, len(qscb.builders))
	nodes := make([]*QuizSubmission, len(qscb.builders))
	mutators := make([]Mutator, len(qscb.builders))
	for i := range qscb.builders {
		func(i int, root context.Context) {
			builder := qscb.builders[i]
			var mut Mutator = MutateFunc(func(ctx context.Context, m Mutation) (Value, error) {
				mutation, ok := m.(*QuizSubmissionMutation)
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
					_, err = mutators[i+1].Mutate(root, qscb.builders[i+1].mutation)
				} else {
					spec := &sqlgraph.BatchCreateSpec{Nodes: specs}
					// Invoke the actual operation on the latest mutation in the chain.
					if err = sqlgraph.BatchCreate(ctx, qscb.driver, spec); err != nil {
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
		if _, err := mutators[0].Mutate(ctx, qscb.builders[0].mutation); err != nil {
			return nil, err
		}
	}
	return nodes, nil
}

// SaveX is like Save, but panics if an error occurs.
func (qscb *QuizSubmissionCreateBulk) SaveX(ctx context.Context) []*QuizSubmission {
	v, err := qscb.Save(ctx)
	if err != nil {
		panic(err)
	}
	return v
}

// Exec executes the query.
func (qscb *QuizSubmissionCreateBulk) Exec(ctx context.Context) error {
	_, err := qscb.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (qscb *QuizSubmissionCreateBulk) ExecX(ctx context.Context) {
	if err := qscb.Exec(ctx); err != nil {
		panic(err)
	}
}
