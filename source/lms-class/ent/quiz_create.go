// Code generated by ent, DO NOT EDIT.

package ent

import (
	"context"
	"errors"
	"fmt"
	"lms-class/ent/exam"
	"lms-class/ent/quiz"
	"lms-class/ent/quizsubmission"
	"time"

	"entgo.io/ent/dialect/sql/sqlgraph"
	"entgo.io/ent/schema/field"
)

// QuizCreate is the builder for creating a Quiz entity.
type QuizCreate struct {
	config
	mutation *QuizMutation
	hooks    []Hook
}

// SetTitle sets the "title" field.
func (qc *QuizCreate) SetTitle(s string) *QuizCreate {
	qc.mutation.SetTitle(s)
	return qc
}

// SetDescription sets the "description" field.
func (qc *QuizCreate) SetDescription(s string) *QuizCreate {
	qc.mutation.SetDescription(s)
	return qc
}

// SetGradeTag sets the "gradeTag" field.
func (qc *QuizCreate) SetGradeTag(s string) *QuizCreate {
	qc.mutation.SetGradeTag(s)
	return qc
}

// SetExamId sets the "examId" field.
func (qc *QuizCreate) SetExamId(i int) *QuizCreate {
	qc.mutation.SetExamId(i)
	return qc
}

// SetNillableExamId sets the "examId" field if the given value is not nil.
func (qc *QuizCreate) SetNillableExamId(i *int) *QuizCreate {
	if i != nil {
		qc.SetExamId(*i)
	}
	return qc
}

// SetIsPublished sets the "isPublished" field.
func (qc *QuizCreate) SetIsPublished(b bool) *QuizCreate {
	qc.mutation.SetIsPublished(b)
	return qc
}

// SetCreatedAt sets the "createdAt" field.
func (qc *QuizCreate) SetCreatedAt(t time.Time) *QuizCreate {
	qc.mutation.SetCreatedAt(t)
	return qc
}

// SetUpdatedAt sets the "updatedAt" field.
func (qc *QuizCreate) SetUpdatedAt(t time.Time) *QuizCreate {
	qc.mutation.SetUpdatedAt(t)
	return qc
}

// SetContext sets the "context" field.
func (qc *QuizCreate) SetContext(s string) *QuizCreate {
	qc.mutation.SetContext(s)
	return qc
}

// SetContextId sets the "contextId" field.
func (qc *QuizCreate) SetContextId(i int) *QuizCreate {
	qc.mutation.SetContextId(i)
	return qc
}

// SetParentId sets the "parentId" field.
func (qc *QuizCreate) SetParentId(i int) *QuizCreate {
	qc.mutation.SetParentId(i)
	return qc
}

// SetNillableParentId sets the "parentId" field if the given value is not nil.
func (qc *QuizCreate) SetNillableParentId(i *int) *QuizCreate {
	if i != nil {
		qc.SetParentId(*i)
	}
	return qc
}

// SetStartedAt sets the "startedAt" field.
func (qc *QuizCreate) SetStartedAt(t time.Time) *QuizCreate {
	qc.mutation.SetStartedAt(t)
	return qc
}

// SetNillableStartedAt sets the "startedAt" field if the given value is not nil.
func (qc *QuizCreate) SetNillableStartedAt(t *time.Time) *QuizCreate {
	if t != nil {
		qc.SetStartedAt(*t)
	}
	return qc
}

// SetFinishedAt sets the "finishedAt" field.
func (qc *QuizCreate) SetFinishedAt(t time.Time) *QuizCreate {
	qc.mutation.SetFinishedAt(t)
	return qc
}

// SetNillableFinishedAt sets the "finishedAt" field if the given value is not nil.
func (qc *QuizCreate) SetNillableFinishedAt(t *time.Time) *QuizCreate {
	if t != nil {
		qc.SetFinishedAt(*t)
	}
	return qc
}

// SetTimeLimit sets the "timeLimit" field.
func (qc *QuizCreate) SetTimeLimit(i int) *QuizCreate {
	qc.mutation.SetTimeLimit(i)
	return qc
}

// SetNillableTimeLimit sets the "timeLimit" field if the given value is not nil.
func (qc *QuizCreate) SetNillableTimeLimit(i *int) *QuizCreate {
	if i != nil {
		qc.SetTimeLimit(*i)
	}
	return qc
}

// SetMaxAttempt sets the "maxAttempt" field.
func (qc *QuizCreate) SetMaxAttempt(i int) *QuizCreate {
	qc.mutation.SetMaxAttempt(i)
	return qc
}

// SetNillableMaxAttempt sets the "maxAttempt" field if the given value is not nil.
func (qc *QuizCreate) SetNillableMaxAttempt(i *int) *QuizCreate {
	if i != nil {
		qc.SetMaxAttempt(*i)
	}
	return qc
}

// SetViewPreviousSessions sets the "viewPreviousSessions" field.
func (qc *QuizCreate) SetViewPreviousSessions(b bool) *QuizCreate {
	qc.mutation.SetViewPreviousSessions(b)
	return qc
}

// SetViewPreviousSessionsTime sets the "viewPreviousSessionsTime" field.
func (qc *QuizCreate) SetViewPreviousSessionsTime(t time.Time) *QuizCreate {
	qc.mutation.SetViewPreviousSessionsTime(t)
	return qc
}

// SetNillableViewPreviousSessionsTime sets the "viewPreviousSessionsTime" field if the given value is not nil.
func (qc *QuizCreate) SetNillableViewPreviousSessionsTime(t *time.Time) *QuizCreate {
	if t != nil {
		qc.SetViewPreviousSessionsTime(*t)
	}
	return qc
}

// SetViewResult sets the "viewResult" field.
func (qc *QuizCreate) SetViewResult(b bool) *QuizCreate {
	qc.mutation.SetViewResult(b)
	return qc
}

// SetPassedScore sets the "passedScore" field.
func (qc *QuizCreate) SetPassedScore(i int) *QuizCreate {
	qc.mutation.SetPassedScore(i)
	return qc
}

// SetNillablePassedScore sets the "passedScore" field if the given value is not nil.
func (qc *QuizCreate) SetNillablePassedScore(i *int) *QuizCreate {
	if i != nil {
		qc.SetPassedScore(*i)
	}
	return qc
}

// SetFinalGradedStrategy sets the "finalGradedStrategy" field.
func (qc *QuizCreate) SetFinalGradedStrategy(s string) *QuizCreate {
	qc.mutation.SetFinalGradedStrategy(s)
	return qc
}

// SetNillableFinalGradedStrategy sets the "finalGradedStrategy" field if the given value is not nil.
func (qc *QuizCreate) SetNillableFinalGradedStrategy(s *string) *QuizCreate {
	if s != nil {
		qc.SetFinalGradedStrategy(*s)
	}
	return qc
}

// SetExamID sets the "exam" edge to the Exam entity by ID.
func (qc *QuizCreate) SetExamID(id int) *QuizCreate {
	qc.mutation.SetExamID(id)
	return qc
}

// SetNillableExamID sets the "exam" edge to the Exam entity by ID if the given value is not nil.
func (qc *QuizCreate) SetNillableExamID(id *int) *QuizCreate {
	if id != nil {
		qc = qc.SetExamID(*id)
	}
	return qc
}

// SetExam sets the "exam" edge to the Exam entity.
func (qc *QuizCreate) SetExam(e *Exam) *QuizCreate {
	return qc.SetExamID(e.ID)
}

// AddSubmissionIDs adds the "submissions" edge to the QuizSubmission entity by IDs.
func (qc *QuizCreate) AddSubmissionIDs(ids ...int) *QuizCreate {
	qc.mutation.AddSubmissionIDs(ids...)
	return qc
}

// AddSubmissions adds the "submissions" edges to the QuizSubmission entity.
func (qc *QuizCreate) AddSubmissions(q ...*QuizSubmission) *QuizCreate {
	ids := make([]int, len(q))
	for i := range q {
		ids[i] = q[i].ID
	}
	return qc.AddSubmissionIDs(ids...)
}

// Mutation returns the QuizMutation object of the builder.
func (qc *QuizCreate) Mutation() *QuizMutation {
	return qc.mutation
}

// Save creates the Quiz in the database.
func (qc *QuizCreate) Save(ctx context.Context) (*Quiz, error) {
	return withHooks(ctx, qc.sqlSave, qc.mutation, qc.hooks)
}

// SaveX calls Save and panics if Save returns an error.
func (qc *QuizCreate) SaveX(ctx context.Context) *Quiz {
	v, err := qc.Save(ctx)
	if err != nil {
		panic(err)
	}
	return v
}

// Exec executes the query.
func (qc *QuizCreate) Exec(ctx context.Context) error {
	_, err := qc.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (qc *QuizCreate) ExecX(ctx context.Context) {
	if err := qc.Exec(ctx); err != nil {
		panic(err)
	}
}

// check runs all checks and user-defined validators on the builder.
func (qc *QuizCreate) check() error {
	if _, ok := qc.mutation.Title(); !ok {
		return &ValidationError{Name: "title", err: errors.New(`ent: missing required field "Quiz.title"`)}
	}
	if _, ok := qc.mutation.Description(); !ok {
		return &ValidationError{Name: "description", err: errors.New(`ent: missing required field "Quiz.description"`)}
	}
	if _, ok := qc.mutation.GradeTag(); !ok {
		return &ValidationError{Name: "gradeTag", err: errors.New(`ent: missing required field "Quiz.gradeTag"`)}
	}
	if _, ok := qc.mutation.IsPublished(); !ok {
		return &ValidationError{Name: "isPublished", err: errors.New(`ent: missing required field "Quiz.isPublished"`)}
	}
	if _, ok := qc.mutation.CreatedAt(); !ok {
		return &ValidationError{Name: "createdAt", err: errors.New(`ent: missing required field "Quiz.createdAt"`)}
	}
	if _, ok := qc.mutation.UpdatedAt(); !ok {
		return &ValidationError{Name: "updatedAt", err: errors.New(`ent: missing required field "Quiz.updatedAt"`)}
	}
	if _, ok := qc.mutation.Context(); !ok {
		return &ValidationError{Name: "context", err: errors.New(`ent: missing required field "Quiz.context"`)}
	}
	if _, ok := qc.mutation.ContextId(); !ok {
		return &ValidationError{Name: "contextId", err: errors.New(`ent: missing required field "Quiz.contextId"`)}
	}
	if _, ok := qc.mutation.ViewPreviousSessions(); !ok {
		return &ValidationError{Name: "viewPreviousSessions", err: errors.New(`ent: missing required field "Quiz.viewPreviousSessions"`)}
	}
	if _, ok := qc.mutation.ViewResult(); !ok {
		return &ValidationError{Name: "viewResult", err: errors.New(`ent: missing required field "Quiz.viewResult"`)}
	}
	return nil
}

func (qc *QuizCreate) sqlSave(ctx context.Context) (*Quiz, error) {
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

func (qc *QuizCreate) createSpec() (*Quiz, *sqlgraph.CreateSpec) {
	var (
		_node = &Quiz{config: qc.config}
		_spec = sqlgraph.NewCreateSpec(quiz.Table, sqlgraph.NewFieldSpec(quiz.FieldID, field.TypeInt))
	)
	if value, ok := qc.mutation.Title(); ok {
		_spec.SetField(quiz.FieldTitle, field.TypeString, value)
		_node.Title = value
	}
	if value, ok := qc.mutation.Description(); ok {
		_spec.SetField(quiz.FieldDescription, field.TypeString, value)
		_node.Description = value
	}
	if value, ok := qc.mutation.GradeTag(); ok {
		_spec.SetField(quiz.FieldGradeTag, field.TypeString, value)
		_node.GradeTag = value
	}
	if value, ok := qc.mutation.IsPublished(); ok {
		_spec.SetField(quiz.FieldIsPublished, field.TypeBool, value)
		_node.IsPublished = value
	}
	if value, ok := qc.mutation.CreatedAt(); ok {
		_spec.SetField(quiz.FieldCreatedAt, field.TypeTime, value)
		_node.CreatedAt = value
	}
	if value, ok := qc.mutation.UpdatedAt(); ok {
		_spec.SetField(quiz.FieldUpdatedAt, field.TypeTime, value)
		_node.UpdatedAt = value
	}
	if value, ok := qc.mutation.Context(); ok {
		_spec.SetField(quiz.FieldContext, field.TypeString, value)
		_node.Context = value
	}
	if value, ok := qc.mutation.ContextId(); ok {
		_spec.SetField(quiz.FieldContextId, field.TypeInt, value)
		_node.ContextId = value
	}
	if value, ok := qc.mutation.ParentId(); ok {
		_spec.SetField(quiz.FieldParentId, field.TypeInt, value)
		_node.ParentId = &value
	}
	if value, ok := qc.mutation.StartedAt(); ok {
		_spec.SetField(quiz.FieldStartedAt, field.TypeTime, value)
		_node.StartedAt = &value
	}
	if value, ok := qc.mutation.FinishedAt(); ok {
		_spec.SetField(quiz.FieldFinishedAt, field.TypeTime, value)
		_node.FinishedAt = &value
	}
	if value, ok := qc.mutation.TimeLimit(); ok {
		_spec.SetField(quiz.FieldTimeLimit, field.TypeInt, value)
		_node.TimeLimit = &value
	}
	if value, ok := qc.mutation.MaxAttempt(); ok {
		_spec.SetField(quiz.FieldMaxAttempt, field.TypeInt, value)
		_node.MaxAttempt = &value
	}
	if value, ok := qc.mutation.ViewPreviousSessions(); ok {
		_spec.SetField(quiz.FieldViewPreviousSessions, field.TypeBool, value)
		_node.ViewPreviousSessions = value
	}
	if value, ok := qc.mutation.ViewPreviousSessionsTime(); ok {
		_spec.SetField(quiz.FieldViewPreviousSessionsTime, field.TypeTime, value)
		_node.ViewPreviousSessionsTime = &value
	}
	if value, ok := qc.mutation.ViewResult(); ok {
		_spec.SetField(quiz.FieldViewResult, field.TypeBool, value)
		_node.ViewResult = value
	}
	if value, ok := qc.mutation.PassedScore(); ok {
		_spec.SetField(quiz.FieldPassedScore, field.TypeInt, value)
		_node.PassedScore = &value
	}
	if value, ok := qc.mutation.FinalGradedStrategy(); ok {
		_spec.SetField(quiz.FieldFinalGradedStrategy, field.TypeString, value)
		_node.FinalGradedStrategy = &value
	}
	if nodes := qc.mutation.ExamIDs(); len(nodes) > 0 {
		edge := &sqlgraph.EdgeSpec{
			Rel:     sqlgraph.M2O,
			Inverse: true,
			Table:   quiz.ExamTable,
			Columns: []string{quiz.ExamColumn},
			Bidi:    false,
			Target: &sqlgraph.EdgeTarget{
				IDSpec: sqlgraph.NewFieldSpec(exam.FieldID, field.TypeInt),
			},
		}
		for _, k := range nodes {
			edge.Target.Nodes = append(edge.Target.Nodes, k)
		}
		_node.ExamId = &nodes[0]
		_spec.Edges = append(_spec.Edges, edge)
	}
	if nodes := qc.mutation.SubmissionsIDs(); len(nodes) > 0 {
		edge := &sqlgraph.EdgeSpec{
			Rel:     sqlgraph.O2M,
			Inverse: false,
			Table:   quiz.SubmissionsTable,
			Columns: []string{quiz.SubmissionsColumn},
			Bidi:    false,
			Target: &sqlgraph.EdgeTarget{
				IDSpec: sqlgraph.NewFieldSpec(quizsubmission.FieldID, field.TypeInt),
			},
		}
		for _, k := range nodes {
			edge.Target.Nodes = append(edge.Target.Nodes, k)
		}
		_spec.Edges = append(_spec.Edges, edge)
	}
	return _node, _spec
}

// QuizCreateBulk is the builder for creating many Quiz entities in bulk.
type QuizCreateBulk struct {
	config
	builders []*QuizCreate
}

// Save creates the Quiz entities in the database.
func (qcb *QuizCreateBulk) Save(ctx context.Context) ([]*Quiz, error) {
	specs := make([]*sqlgraph.CreateSpec, len(qcb.builders))
	nodes := make([]*Quiz, len(qcb.builders))
	mutators := make([]Mutator, len(qcb.builders))
	for i := range qcb.builders {
		func(i int, root context.Context) {
			builder := qcb.builders[i]
			var mut Mutator = MutateFunc(func(ctx context.Context, m Mutation) (Value, error) {
				mutation, ok := m.(*QuizMutation)
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
func (qcb *QuizCreateBulk) SaveX(ctx context.Context) []*Quiz {
	v, err := qcb.Save(ctx)
	if err != nil {
		panic(err)
	}
	return v
}

// Exec executes the query.
func (qcb *QuizCreateBulk) Exec(ctx context.Context) error {
	_, err := qcb.Save(ctx)
	return err
}

// ExecX is like Exec, but panics if an error occurs.
func (qcb *QuizCreateBulk) ExecX(ctx context.Context) {
	if err := qcb.Exec(ctx); err != nil {
		panic(err)
	}
}
