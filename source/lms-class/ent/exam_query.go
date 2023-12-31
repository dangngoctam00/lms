// Code generated by ent, DO NOT EDIT.

package ent

import (
	"context"
	"database/sql/driver"
	"fmt"
	"lms-class/ent/exam"
	"lms-class/ent/predicate"
	"lms-class/ent/quiz"
	"math"

	"entgo.io/ent/dialect"
	"entgo.io/ent/dialect/sql"
	"entgo.io/ent/dialect/sql/sqlgraph"
	"entgo.io/ent/schema/field"
)

// ExamQuery is the builder for querying Exam entities.
type ExamQuery struct {
	config
	ctx         *QueryContext
	order       []exam.OrderOption
	inters      []Interceptor
	predicates  []predicate.Exam
	withQuizzes *QuizQuery
	modifiers   []func(*sql.Selector)
	// intermediate query (i.e. traversal path).
	sql  *sql.Selector
	path func(context.Context) (*sql.Selector, error)
}

// Where adds a new predicate for the ExamQuery builder.
func (eq *ExamQuery) Where(ps ...predicate.Exam) *ExamQuery {
	eq.predicates = append(eq.predicates, ps...)
	return eq
}

// Limit the number of records to be returned by this query.
func (eq *ExamQuery) Limit(limit int) *ExamQuery {
	eq.ctx.Limit = &limit
	return eq
}

// Offset to start from.
func (eq *ExamQuery) Offset(offset int) *ExamQuery {
	eq.ctx.Offset = &offset
	return eq
}

// Unique configures the query builder to filter duplicate records on query.
// By default, unique is set to true, and can be disabled using this method.
func (eq *ExamQuery) Unique(unique bool) *ExamQuery {
	eq.ctx.Unique = &unique
	return eq
}

// Order specifies how the records should be ordered.
func (eq *ExamQuery) Order(o ...exam.OrderOption) *ExamQuery {
	eq.order = append(eq.order, o...)
	return eq
}

// QueryQuizzes chains the current query on the "quizzes" edge.
func (eq *ExamQuery) QueryQuizzes() *QuizQuery {
	query := (&QuizClient{config: eq.config}).Query()
	query.path = func(ctx context.Context) (fromU *sql.Selector, err error) {
		if err := eq.prepareQuery(ctx); err != nil {
			return nil, err
		}
		selector := eq.sqlQuery(ctx)
		if err := selector.Err(); err != nil {
			return nil, err
		}
		step := sqlgraph.NewStep(
			sqlgraph.From(exam.Table, exam.FieldID, selector),
			sqlgraph.To(quiz.Table, quiz.FieldID),
			sqlgraph.Edge(sqlgraph.O2M, false, exam.QuizzesTable, exam.QuizzesColumn),
		)
		fromU = sqlgraph.SetNeighbors(eq.driver.Dialect(), step)
		return fromU, nil
	}
	return query
}

// First returns the first Exam entity from the query.
// Returns a *NotFoundError when no Exam was found.
func (eq *ExamQuery) First(ctx context.Context) (*Exam, error) {
	nodes, err := eq.Limit(1).All(setContextOp(ctx, eq.ctx, "First"))
	if err != nil {
		return nil, err
	}
	if len(nodes) == 0 {
		return nil, &NotFoundError{exam.Label}
	}
	return nodes[0], nil
}

// FirstX is like First, but panics if an error occurs.
func (eq *ExamQuery) FirstX(ctx context.Context) *Exam {
	node, err := eq.First(ctx)
	if err != nil && !IsNotFound(err) {
		panic(err)
	}
	return node
}

// FirstID returns the first Exam ID from the query.
// Returns a *NotFoundError when no Exam ID was found.
func (eq *ExamQuery) FirstID(ctx context.Context) (id int, err error) {
	var ids []int
	if ids, err = eq.Limit(1).IDs(setContextOp(ctx, eq.ctx, "FirstID")); err != nil {
		return
	}
	if len(ids) == 0 {
		err = &NotFoundError{exam.Label}
		return
	}
	return ids[0], nil
}

// FirstIDX is like FirstID, but panics if an error occurs.
func (eq *ExamQuery) FirstIDX(ctx context.Context) int {
	id, err := eq.FirstID(ctx)
	if err != nil && !IsNotFound(err) {
		panic(err)
	}
	return id
}

// Only returns a single Exam entity found by the query, ensuring it only returns one.
// Returns a *NotSingularError when more than one Exam entity is found.
// Returns a *NotFoundError when no Exam entities are found.
func (eq *ExamQuery) Only(ctx context.Context) (*Exam, error) {
	nodes, err := eq.Limit(2).All(setContextOp(ctx, eq.ctx, "Only"))
	if err != nil {
		return nil, err
	}
	switch len(nodes) {
	case 1:
		return nodes[0], nil
	case 0:
		return nil, &NotFoundError{exam.Label}
	default:
		return nil, &NotSingularError{exam.Label}
	}
}

// OnlyX is like Only, but panics if an error occurs.
func (eq *ExamQuery) OnlyX(ctx context.Context) *Exam {
	node, err := eq.Only(ctx)
	if err != nil {
		panic(err)
	}
	return node
}

// OnlyID is like Only, but returns the only Exam ID in the query.
// Returns a *NotSingularError when more than one Exam ID is found.
// Returns a *NotFoundError when no entities are found.
func (eq *ExamQuery) OnlyID(ctx context.Context) (id int, err error) {
	var ids []int
	if ids, err = eq.Limit(2).IDs(setContextOp(ctx, eq.ctx, "OnlyID")); err != nil {
		return
	}
	switch len(ids) {
	case 1:
		id = ids[0]
	case 0:
		err = &NotFoundError{exam.Label}
	default:
		err = &NotSingularError{exam.Label}
	}
	return
}

// OnlyIDX is like OnlyID, but panics if an error occurs.
func (eq *ExamQuery) OnlyIDX(ctx context.Context) int {
	id, err := eq.OnlyID(ctx)
	if err != nil {
		panic(err)
	}
	return id
}

// All executes the query and returns a list of Exams.
func (eq *ExamQuery) All(ctx context.Context) ([]*Exam, error) {
	ctx = setContextOp(ctx, eq.ctx, "All")
	if err := eq.prepareQuery(ctx); err != nil {
		return nil, err
	}
	qr := querierAll[[]*Exam, *ExamQuery]()
	return withInterceptors[[]*Exam](ctx, eq, qr, eq.inters)
}

// AllX is like All, but panics if an error occurs.
func (eq *ExamQuery) AllX(ctx context.Context) []*Exam {
	nodes, err := eq.All(ctx)
	if err != nil {
		panic(err)
	}
	return nodes
}

// IDs executes the query and returns a list of Exam IDs.
func (eq *ExamQuery) IDs(ctx context.Context) (ids []int, err error) {
	if eq.ctx.Unique == nil && eq.path != nil {
		eq.Unique(true)
	}
	ctx = setContextOp(ctx, eq.ctx, "IDs")
	if err = eq.Select(exam.FieldID).Scan(ctx, &ids); err != nil {
		return nil, err
	}
	return ids, nil
}

// IDsX is like IDs, but panics if an error occurs.
func (eq *ExamQuery) IDsX(ctx context.Context) []int {
	ids, err := eq.IDs(ctx)
	if err != nil {
		panic(err)
	}
	return ids
}

// Count returns the count of the given query.
func (eq *ExamQuery) Count(ctx context.Context) (int, error) {
	ctx = setContextOp(ctx, eq.ctx, "Count")
	if err := eq.prepareQuery(ctx); err != nil {
		return 0, err
	}
	return withInterceptors[int](ctx, eq, querierCount[*ExamQuery](), eq.inters)
}

// CountX is like Count, but panics if an error occurs.
func (eq *ExamQuery) CountX(ctx context.Context) int {
	count, err := eq.Count(ctx)
	if err != nil {
		panic(err)
	}
	return count
}

// Exist returns true if the query has elements in the graph.
func (eq *ExamQuery) Exist(ctx context.Context) (bool, error) {
	ctx = setContextOp(ctx, eq.ctx, "Exist")
	switch _, err := eq.FirstID(ctx); {
	case IsNotFound(err):
		return false, nil
	case err != nil:
		return false, fmt.Errorf("ent: check existence: %w", err)
	default:
		return true, nil
	}
}

// ExistX is like Exist, but panics if an error occurs.
func (eq *ExamQuery) ExistX(ctx context.Context) bool {
	exist, err := eq.Exist(ctx)
	if err != nil {
		panic(err)
	}
	return exist
}

// Clone returns a duplicate of the ExamQuery builder, including all associated steps. It can be
// used to prepare common query builders and use them differently after the clone is made.
func (eq *ExamQuery) Clone() *ExamQuery {
	if eq == nil {
		return nil
	}
	return &ExamQuery{
		config:      eq.config,
		ctx:         eq.ctx.Clone(),
		order:       append([]exam.OrderOption{}, eq.order...),
		inters:      append([]Interceptor{}, eq.inters...),
		predicates:  append([]predicate.Exam{}, eq.predicates...),
		withQuizzes: eq.withQuizzes.Clone(),
		// clone intermediate query.
		sql:  eq.sql.Clone(),
		path: eq.path,
	}
}

// WithQuizzes tells the query-builder to eager-load the nodes that are connected to
// the "quizzes" edge. The optional arguments are used to configure the query builder of the edge.
func (eq *ExamQuery) WithQuizzes(opts ...func(*QuizQuery)) *ExamQuery {
	query := (&QuizClient{config: eq.config}).Query()
	for _, opt := range opts {
		opt(query)
	}
	eq.withQuizzes = query
	return eq
}

// GroupBy is used to group vertices by one or more fields/columns.
// It is often used with aggregate functions, like: count, max, mean, min, sum.
//
// Example:
//
//	var v []struct {
//		Title string `json:"title,omitempty"`
//		Count int `json:"count,omitempty"`
//	}
//
//	client.Exam.Query().
//		GroupBy(exam.FieldTitle).
//		Aggregate(ent.Count()).
//		Scan(ctx, &v)
func (eq *ExamQuery) GroupBy(field string, fields ...string) *ExamGroupBy {
	eq.ctx.Fields = append([]string{field}, fields...)
	grbuild := &ExamGroupBy{build: eq}
	grbuild.flds = &eq.ctx.Fields
	grbuild.label = exam.Label
	grbuild.scan = grbuild.Scan
	return grbuild
}

// Select allows the selection one or more fields/columns for the given query,
// instead of selecting all fields in the entity.
//
// Example:
//
//	var v []struct {
//		Title string `json:"title,omitempty"`
//	}
//
//	client.Exam.Query().
//		Select(exam.FieldTitle).
//		Scan(ctx, &v)
func (eq *ExamQuery) Select(fields ...string) *ExamSelect {
	eq.ctx.Fields = append(eq.ctx.Fields, fields...)
	sbuild := &ExamSelect{ExamQuery: eq}
	sbuild.label = exam.Label
	sbuild.flds, sbuild.scan = &eq.ctx.Fields, sbuild.Scan
	return sbuild
}

// Aggregate returns a ExamSelect configured with the given aggregations.
func (eq *ExamQuery) Aggregate(fns ...AggregateFunc) *ExamSelect {
	return eq.Select().Aggregate(fns...)
}

func (eq *ExamQuery) prepareQuery(ctx context.Context) error {
	for _, inter := range eq.inters {
		if inter == nil {
			return fmt.Errorf("ent: uninitialized interceptor (forgotten import ent/runtime?)")
		}
		if trv, ok := inter.(Traverser); ok {
			if err := trv.Traverse(ctx, eq); err != nil {
				return err
			}
		}
	}
	for _, f := range eq.ctx.Fields {
		if !exam.ValidColumn(f) {
			return &ValidationError{Name: f, err: fmt.Errorf("ent: invalid field %q for query", f)}
		}
	}
	if eq.path != nil {
		prev, err := eq.path(ctx)
		if err != nil {
			return err
		}
		eq.sql = prev
	}
	return nil
}

func (eq *ExamQuery) sqlAll(ctx context.Context, hooks ...queryHook) ([]*Exam, error) {
	var (
		nodes       = []*Exam{}
		_spec       = eq.querySpec()
		loadedTypes = [1]bool{
			eq.withQuizzes != nil,
		}
	)
	_spec.ScanValues = func(columns []string) ([]any, error) {
		return (*Exam).scanValues(nil, columns)
	}
	_spec.Assign = func(columns []string, values []any) error {
		node := &Exam{config: eq.config}
		nodes = append(nodes, node)
		node.Edges.loadedTypes = loadedTypes
		return node.assignValues(columns, values)
	}
	if len(eq.modifiers) > 0 {
		_spec.Modifiers = eq.modifiers
	}
	for i := range hooks {
		hooks[i](ctx, _spec)
	}
	if err := sqlgraph.QueryNodes(ctx, eq.driver, _spec); err != nil {
		return nil, err
	}
	if len(nodes) == 0 {
		return nodes, nil
	}
	if query := eq.withQuizzes; query != nil {
		if err := eq.loadQuizzes(ctx, query, nodes,
			func(n *Exam) { n.Edges.Quizzes = []*Quiz{} },
			func(n *Exam, e *Quiz) { n.Edges.Quizzes = append(n.Edges.Quizzes, e) }); err != nil {
			return nil, err
		}
	}
	return nodes, nil
}

func (eq *ExamQuery) loadQuizzes(ctx context.Context, query *QuizQuery, nodes []*Exam, init func(*Exam), assign func(*Exam, *Quiz)) error {
	fks := make([]driver.Value, 0, len(nodes))
	nodeids := make(map[int]*Exam)
	for i := range nodes {
		fks = append(fks, nodes[i].ID)
		nodeids[nodes[i].ID] = nodes[i]
		if init != nil {
			init(nodes[i])
		}
	}
	if len(query.ctx.Fields) > 0 {
		query.ctx.AppendFieldOnce(quiz.FieldExamId)
	}
	query.Where(predicate.Quiz(func(s *sql.Selector) {
		s.Where(sql.InValues(s.C(exam.QuizzesColumn), fks...))
	}))
	neighbors, err := query.All(ctx)
	if err != nil {
		return err
	}
	for _, n := range neighbors {
		fk := n.ExamId
		if fk == nil {
			return fmt.Errorf(`foreign-key "examId" is nil for node %v`, n.ID)
		}
		node, ok := nodeids[*fk]
		if !ok {
			return fmt.Errorf(`unexpected referenced foreign-key "examId" returned %v for node %v`, *fk, n.ID)
		}
		assign(node, n)
	}
	return nil
}

func (eq *ExamQuery) sqlCount(ctx context.Context) (int, error) {
	_spec := eq.querySpec()
	if len(eq.modifiers) > 0 {
		_spec.Modifiers = eq.modifiers
	}
	_spec.Node.Columns = eq.ctx.Fields
	if len(eq.ctx.Fields) > 0 {
		_spec.Unique = eq.ctx.Unique != nil && *eq.ctx.Unique
	}
	return sqlgraph.CountNodes(ctx, eq.driver, _spec)
}

func (eq *ExamQuery) querySpec() *sqlgraph.QuerySpec {
	_spec := sqlgraph.NewQuerySpec(exam.Table, exam.Columns, sqlgraph.NewFieldSpec(exam.FieldID, field.TypeInt))
	_spec.From = eq.sql
	if unique := eq.ctx.Unique; unique != nil {
		_spec.Unique = *unique
	} else if eq.path != nil {
		_spec.Unique = true
	}
	if fields := eq.ctx.Fields; len(fields) > 0 {
		_spec.Node.Columns = make([]string, 0, len(fields))
		_spec.Node.Columns = append(_spec.Node.Columns, exam.FieldID)
		for i := range fields {
			if fields[i] != exam.FieldID {
				_spec.Node.Columns = append(_spec.Node.Columns, fields[i])
			}
		}
	}
	if ps := eq.predicates; len(ps) > 0 {
		_spec.Predicate = func(selector *sql.Selector) {
			for i := range ps {
				ps[i](selector)
			}
		}
	}
	if limit := eq.ctx.Limit; limit != nil {
		_spec.Limit = *limit
	}
	if offset := eq.ctx.Offset; offset != nil {
		_spec.Offset = *offset
	}
	if ps := eq.order; len(ps) > 0 {
		_spec.Order = func(selector *sql.Selector) {
			for i := range ps {
				ps[i](selector)
			}
		}
	}
	return _spec
}

func (eq *ExamQuery) sqlQuery(ctx context.Context) *sql.Selector {
	builder := sql.Dialect(eq.driver.Dialect())
	t1 := builder.Table(exam.Table)
	columns := eq.ctx.Fields
	if len(columns) == 0 {
		columns = exam.Columns
	}
	selector := builder.Select(t1.Columns(columns...)...).From(t1)
	if eq.sql != nil {
		selector = eq.sql
		selector.Select(selector.Columns(columns...)...)
	}
	if eq.ctx.Unique != nil && *eq.ctx.Unique {
		selector.Distinct()
	}
	for _, m := range eq.modifiers {
		m(selector)
	}
	for _, p := range eq.predicates {
		p(selector)
	}
	for _, p := range eq.order {
		p(selector)
	}
	if offset := eq.ctx.Offset; offset != nil {
		// limit is mandatory for offset clause. We start
		// with default value, and override it below if needed.
		selector.Offset(*offset).Limit(math.MaxInt32)
	}
	if limit := eq.ctx.Limit; limit != nil {
		selector.Limit(*limit)
	}
	return selector
}

// ForUpdate locks the selected rows against concurrent updates, and prevent them from being
// updated, deleted or "selected ... for update" by other sessions, until the transaction is
// either committed or rolled-back.
func (eq *ExamQuery) ForUpdate(opts ...sql.LockOption) *ExamQuery {
	if eq.driver.Dialect() == dialect.Postgres {
		eq.Unique(false)
	}
	eq.modifiers = append(eq.modifiers, func(s *sql.Selector) {
		s.ForUpdate(opts...)
	})
	return eq
}

// ForShare behaves similarly to ForUpdate, except that it acquires a shared mode lock
// on any rows that are read. Other sessions can read the rows, but cannot modify them
// until your transaction commits.
func (eq *ExamQuery) ForShare(opts ...sql.LockOption) *ExamQuery {
	if eq.driver.Dialect() == dialect.Postgres {
		eq.Unique(false)
	}
	eq.modifiers = append(eq.modifiers, func(s *sql.Selector) {
		s.ForShare(opts...)
	})
	return eq
}

// ExamGroupBy is the group-by builder for Exam entities.
type ExamGroupBy struct {
	selector
	build *ExamQuery
}

// Aggregate adds the given aggregation functions to the group-by query.
func (egb *ExamGroupBy) Aggregate(fns ...AggregateFunc) *ExamGroupBy {
	egb.fns = append(egb.fns, fns...)
	return egb
}

// Scan applies the selector query and scans the result into the given value.
func (egb *ExamGroupBy) Scan(ctx context.Context, v any) error {
	ctx = setContextOp(ctx, egb.build.ctx, "GroupBy")
	if err := egb.build.prepareQuery(ctx); err != nil {
		return err
	}
	return scanWithInterceptors[*ExamQuery, *ExamGroupBy](ctx, egb.build, egb, egb.build.inters, v)
}

func (egb *ExamGroupBy) sqlScan(ctx context.Context, root *ExamQuery, v any) error {
	selector := root.sqlQuery(ctx).Select()
	aggregation := make([]string, 0, len(egb.fns))
	for _, fn := range egb.fns {
		aggregation = append(aggregation, fn(selector))
	}
	if len(selector.SelectedColumns()) == 0 {
		columns := make([]string, 0, len(*egb.flds)+len(egb.fns))
		for _, f := range *egb.flds {
			columns = append(columns, selector.C(f))
		}
		columns = append(columns, aggregation...)
		selector.Select(columns...)
	}
	selector.GroupBy(selector.Columns(*egb.flds...)...)
	if err := selector.Err(); err != nil {
		return err
	}
	rows := &sql.Rows{}
	query, args := selector.Query()
	if err := egb.build.driver.Query(ctx, query, args, rows); err != nil {
		return err
	}
	defer rows.Close()
	return sql.ScanSlice(rows, v)
}

// ExamSelect is the builder for selecting fields of Exam entities.
type ExamSelect struct {
	*ExamQuery
	selector
}

// Aggregate adds the given aggregation functions to the selector query.
func (es *ExamSelect) Aggregate(fns ...AggregateFunc) *ExamSelect {
	es.fns = append(es.fns, fns...)
	return es
}

// Scan applies the selector query and scans the result into the given value.
func (es *ExamSelect) Scan(ctx context.Context, v any) error {
	ctx = setContextOp(ctx, es.ctx, "Select")
	if err := es.prepareQuery(ctx); err != nil {
		return err
	}
	return scanWithInterceptors[*ExamQuery, *ExamSelect](ctx, es.ExamQuery, es, es.inters, v)
}

func (es *ExamSelect) sqlScan(ctx context.Context, root *ExamQuery, v any) error {
	selector := root.sqlQuery(ctx)
	aggregation := make([]string, 0, len(es.fns))
	for _, fn := range es.fns {
		aggregation = append(aggregation, fn(selector))
	}
	switch n := len(*es.selector.flds); {
	case n == 0 && len(aggregation) > 0:
		selector.Select(aggregation...)
	case n != 0 && len(aggregation) > 0:
		selector.AppendSelect(aggregation...)
	}
	rows := &sql.Rows{}
	query, args := selector.Query()
	if err := es.driver.Query(ctx, query, args, rows); err != nil {
		return err
	}
	defer rows.Close()
	return sql.ScanSlice(rows, v)
}
