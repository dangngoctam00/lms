// Code generated by ent, DO NOT EDIT.

package intercept

import (
	"context"
	"fmt"
	"lms-class/ent"
	"lms-class/ent/exam"
	"lms-class/ent/examhistory"
	"lms-class/ent/predicate"
	entquestion "lms-class/ent/question"
	"lms-class/ent/questionhistory"
	"lms-class/ent/quiz"
	"lms-class/ent/quizsubmission"

	"entgo.io/ent/dialect/sql"
)

// The Query interface represents an operation that queries a graph.
// By using this interface, users can write generic code that manipulates
// query builders of different types.
type Query interface {
	// Type returns the string representation of the query type.
	Type() string
	// Limit the number of records to be returned by this query.
	Limit(int)
	// Offset to start from.
	Offset(int)
	// Unique configures the query builder to filter duplicate records.
	Unique(bool)
	// Order specifies how the records should be ordered.
	Order(...func(*sql.Selector))
	// WhereP appends storage-level predicates to the query builder. Using this method, users
	// can use type-assertion to append predicates that do not depend on any generated package.
	WhereP(...func(*sql.Selector))
}

// The Func type is an adapter that allows ordinary functions to be used as interceptors.
// Unlike traversal functions, interceptors are skipped during graph traversals. Note that the
// implementation of Func is different from the one defined in entgo.io/ent.InterceptFunc.
type Func func(context.Context, Query) error

// Intercept calls f(ctx, q) and then applied the next Querier.
func (f Func) Intercept(next ent.Querier) ent.Querier {
	return ent.QuerierFunc(func(ctx context.Context, q ent.Query) (ent.Value, error) {
		query, err := NewQuery(q)
		if err != nil {
			return nil, err
		}
		if err := f(ctx, query); err != nil {
			return nil, err
		}
		return next.Query(ctx, q)
	})
}

// The TraverseFunc type is an adapter to allow the use of ordinary function as Traverser.
// If f is a function with the appropriate signature, TraverseFunc(f) is a Traverser that calls f.
type TraverseFunc func(context.Context, Query) error

// Intercept is a dummy implementation of Intercept that returns the next Querier in the pipeline.
func (f TraverseFunc) Intercept(next ent.Querier) ent.Querier {
	return next
}

// Traverse calls f(ctx, q).
func (f TraverseFunc) Traverse(ctx context.Context, q ent.Query) error {
	query, err := NewQuery(q)
	if err != nil {
		return err
	}
	return f(ctx, query)
}

// The ExamFunc type is an adapter to allow the use of ordinary function as a Querier.
type ExamFunc func(context.Context, *ent.ExamQuery) (ent.Value, error)

// Query calls f(ctx, q).
func (f ExamFunc) Query(ctx context.Context, q ent.Query) (ent.Value, error) {
	if q, ok := q.(*ent.ExamQuery); ok {
		return f(ctx, q)
	}
	return nil, fmt.Errorf("unexpected query type %T. expect *ent.ExamQuery", q)
}

// The TraverseExam type is an adapter to allow the use of ordinary function as Traverser.
type TraverseExam func(context.Context, *ent.ExamQuery) error

// Intercept is a dummy implementation of Intercept that returns the next Querier in the pipeline.
func (f TraverseExam) Intercept(next ent.Querier) ent.Querier {
	return next
}

// Traverse calls f(ctx, q).
func (f TraverseExam) Traverse(ctx context.Context, q ent.Query) error {
	if q, ok := q.(*ent.ExamQuery); ok {
		return f(ctx, q)
	}
	return fmt.Errorf("unexpected query type %T. expect *ent.ExamQuery", q)
}

// The ExamHistoryFunc type is an adapter to allow the use of ordinary function as a Querier.
type ExamHistoryFunc func(context.Context, *ent.ExamHistoryQuery) (ent.Value, error)

// Query calls f(ctx, q).
func (f ExamHistoryFunc) Query(ctx context.Context, q ent.Query) (ent.Value, error) {
	if q, ok := q.(*ent.ExamHistoryQuery); ok {
		return f(ctx, q)
	}
	return nil, fmt.Errorf("unexpected query type %T. expect *ent.ExamHistoryQuery", q)
}

// The TraverseExamHistory type is an adapter to allow the use of ordinary function as Traverser.
type TraverseExamHistory func(context.Context, *ent.ExamHistoryQuery) error

// Intercept is a dummy implementation of Intercept that returns the next Querier in the pipeline.
func (f TraverseExamHistory) Intercept(next ent.Querier) ent.Querier {
	return next
}

// Traverse calls f(ctx, q).
func (f TraverseExamHistory) Traverse(ctx context.Context, q ent.Query) error {
	if q, ok := q.(*ent.ExamHistoryQuery); ok {
		return f(ctx, q)
	}
	return fmt.Errorf("unexpected query type %T. expect *ent.ExamHistoryQuery", q)
}

// The QuestionFunc type is an adapter to allow the use of ordinary function as a Querier.
type QuestionFunc func(context.Context, *ent.QuestionQuery) (ent.Value, error)

// Query calls f(ctx, q).
func (f QuestionFunc) Query(ctx context.Context, q ent.Query) (ent.Value, error) {
	if q, ok := q.(*ent.QuestionQuery); ok {
		return f(ctx, q)
	}
	return nil, fmt.Errorf("unexpected query type %T. expect *ent.QuestionQuery", q)
}

// The TraverseQuestion type is an adapter to allow the use of ordinary function as Traverser.
type TraverseQuestion func(context.Context, *ent.QuestionQuery) error

// Intercept is a dummy implementation of Intercept that returns the next Querier in the pipeline.
func (f TraverseQuestion) Intercept(next ent.Querier) ent.Querier {
	return next
}

// Traverse calls f(ctx, q).
func (f TraverseQuestion) Traverse(ctx context.Context, q ent.Query) error {
	if q, ok := q.(*ent.QuestionQuery); ok {
		return f(ctx, q)
	}
	return fmt.Errorf("unexpected query type %T. expect *ent.QuestionQuery", q)
}

// The QuestionHistoryFunc type is an adapter to allow the use of ordinary function as a Querier.
type QuestionHistoryFunc func(context.Context, *ent.QuestionHistoryQuery) (ent.Value, error)

// Query calls f(ctx, q).
func (f QuestionHistoryFunc) Query(ctx context.Context, q ent.Query) (ent.Value, error) {
	if q, ok := q.(*ent.QuestionHistoryQuery); ok {
		return f(ctx, q)
	}
	return nil, fmt.Errorf("unexpected query type %T. expect *ent.QuestionHistoryQuery", q)
}

// The TraverseQuestionHistory type is an adapter to allow the use of ordinary function as Traverser.
type TraverseQuestionHistory func(context.Context, *ent.QuestionHistoryQuery) error

// Intercept is a dummy implementation of Intercept that returns the next Querier in the pipeline.
func (f TraverseQuestionHistory) Intercept(next ent.Querier) ent.Querier {
	return next
}

// Traverse calls f(ctx, q).
func (f TraverseQuestionHistory) Traverse(ctx context.Context, q ent.Query) error {
	if q, ok := q.(*ent.QuestionHistoryQuery); ok {
		return f(ctx, q)
	}
	return fmt.Errorf("unexpected query type %T. expect *ent.QuestionHistoryQuery", q)
}

// The QuizFunc type is an adapter to allow the use of ordinary function as a Querier.
type QuizFunc func(context.Context, *ent.QuizQuery) (ent.Value, error)

// Query calls f(ctx, q).
func (f QuizFunc) Query(ctx context.Context, q ent.Query) (ent.Value, error) {
	if q, ok := q.(*ent.QuizQuery); ok {
		return f(ctx, q)
	}
	return nil, fmt.Errorf("unexpected query type %T. expect *ent.QuizQuery", q)
}

// The TraverseQuiz type is an adapter to allow the use of ordinary function as Traverser.
type TraverseQuiz func(context.Context, *ent.QuizQuery) error

// Intercept is a dummy implementation of Intercept that returns the next Querier in the pipeline.
func (f TraverseQuiz) Intercept(next ent.Querier) ent.Querier {
	return next
}

// Traverse calls f(ctx, q).
func (f TraverseQuiz) Traverse(ctx context.Context, q ent.Query) error {
	if q, ok := q.(*ent.QuizQuery); ok {
		return f(ctx, q)
	}
	return fmt.Errorf("unexpected query type %T. expect *ent.QuizQuery", q)
}

// The QuizSubmissionFunc type is an adapter to allow the use of ordinary function as a Querier.
type QuizSubmissionFunc func(context.Context, *ent.QuizSubmissionQuery) (ent.Value, error)

// Query calls f(ctx, q).
func (f QuizSubmissionFunc) Query(ctx context.Context, q ent.Query) (ent.Value, error) {
	if q, ok := q.(*ent.QuizSubmissionQuery); ok {
		return f(ctx, q)
	}
	return nil, fmt.Errorf("unexpected query type %T. expect *ent.QuizSubmissionQuery", q)
}

// The TraverseQuizSubmission type is an adapter to allow the use of ordinary function as Traverser.
type TraverseQuizSubmission func(context.Context, *ent.QuizSubmissionQuery) error

// Intercept is a dummy implementation of Intercept that returns the next Querier in the pipeline.
func (f TraverseQuizSubmission) Intercept(next ent.Querier) ent.Querier {
	return next
}

// Traverse calls f(ctx, q).
func (f TraverseQuizSubmission) Traverse(ctx context.Context, q ent.Query) error {
	if q, ok := q.(*ent.QuizSubmissionQuery); ok {
		return f(ctx, q)
	}
	return fmt.Errorf("unexpected query type %T. expect *ent.QuizSubmissionQuery", q)
}

// NewQuery returns the generic Query interface for the given typed query.
func NewQuery(q ent.Query) (Query, error) {
	switch q := q.(type) {
	case *ent.ExamQuery:
		return &query[*ent.ExamQuery, predicate.Exam, exam.OrderOption]{typ: ent.TypeExam, tq: q}, nil
	case *ent.ExamHistoryQuery:
		return &query[*ent.ExamHistoryQuery, predicate.ExamHistory, examhistory.OrderOption]{typ: ent.TypeExamHistory, tq: q}, nil
	case *ent.QuestionQuery:
		return &query[*ent.QuestionQuery, predicate.Question, entquestion.OrderOption]{typ: ent.TypeQuestion, tq: q}, nil
	case *ent.QuestionHistoryQuery:
		return &query[*ent.QuestionHistoryQuery, predicate.QuestionHistory, questionhistory.OrderOption]{typ: ent.TypeQuestionHistory, tq: q}, nil
	case *ent.QuizQuery:
		return &query[*ent.QuizQuery, predicate.Quiz, quiz.OrderOption]{typ: ent.TypeQuiz, tq: q}, nil
	case *ent.QuizSubmissionQuery:
		return &query[*ent.QuizSubmissionQuery, predicate.QuizSubmission, quizsubmission.OrderOption]{typ: ent.TypeQuizSubmission, tq: q}, nil
	default:
		return nil, fmt.Errorf("unknown query type %T", q)
	}
}

type query[T any, P ~func(*sql.Selector), R ~func(*sql.Selector)] struct {
	typ string
	tq  interface {
		Limit(int) T
		Offset(int) T
		Unique(bool) T
		Order(...R) T
		Where(...P) T
	}
}

func (q query[T, P, R]) Type() string {
	return q.typ
}

func (q query[T, P, R]) Limit(limit int) {
	q.tq.Limit(limit)
}

func (q query[T, P, R]) Offset(offset int) {
	q.tq.Offset(offset)
}

func (q query[T, P, R]) Unique(unique bool) {
	q.tq.Unique(unique)
}

func (q query[T, P, R]) Order(orders ...func(*sql.Selector)) {
	rs := make([]R, len(orders))
	for i := range orders {
		rs[i] = orders[i]
	}
	q.tq.Order(rs...)
}

func (q query[T, P, R]) WhereP(ps ...func(*sql.Selector)) {
	p := make([]P, len(ps))
	for i := range ps {
		p[i] = ps[i]
	}
	q.tq.Where(p...)
}
