// Code generated by ent, DO NOT EDIT.

package ent

import (
	"context"
	"errors"
	"fmt"
	"log"

	"lms-class/ent/migrate"

	"lms-class/ent/exam"

	"entgo.io/ent"
	"entgo.io/ent/dialect"
	"entgo.io/ent/dialect/sql"
)

// Client is the client that holds all ent builders.
type Client struct {
	config
	// Schema is the client for creating, migrating and dropping schema.
	Schema *migrate.Schema
	// Exam is the client for interacting with the Exam builders.
	Exam *ExamClient
}

// NewClient creates a new client configured with the given options.
func NewClient(opts ...Option) *Client {
	cfg := config{log: log.Println, hooks: &hooks{}, inters: &inters{}}
	cfg.options(opts...)
	client := &Client{config: cfg}
	client.init()
	return client
}

func (c *Client) init() {
	c.Schema = migrate.NewSchema(c.driver)
	c.Exam = NewExamClient(c.config)
}

type (
	// config is the configuration for the client and its builder.
	config struct {
		// driver used for executing database requests.
		driver dialect.Driver
		// debug enable a debug logging.
		debug bool
		// log used for logging on debug mode.
		log func(...any)
		// hooks to execute on mutations.
		hooks *hooks
		// interceptors to execute on queries.
		inters *inters
	}
	// Option function to configure the client.
	Option func(*config)
)

// options applies the options on the config object.
func (c *config) options(opts ...Option) {
	for _, opt := range opts {
		opt(c)
	}
	if c.debug {
		c.driver = dialect.Debug(c.driver, c.log)
	}
}

// Debug enables debug logging on the ent.Driver.
func Debug() Option {
	return func(c *config) {
		c.debug = true
	}
}

// Log sets the logging function for debug mode.
func Log(fn func(...any)) Option {
	return func(c *config) {
		c.log = fn
	}
}

// Driver configures the client driver.
func Driver(driver dialect.Driver) Option {
	return func(c *config) {
		c.driver = driver
	}
}

// Open opens a database/sql.DB specified by the driver name and
// the data source name, and returns a new client attached to it.
// Optional parameters can be added for configuring the client.
func Open(driverName, dataSourceName string, options ...Option) (*Client, error) {
	switch driverName {
	case dialect.MySQL, dialect.Postgres, dialect.SQLite:
		drv, err := sql.Open(driverName, dataSourceName)
		if err != nil {
			return nil, err
		}
		return NewClient(append(options, Driver(drv))...), nil
	default:
		return nil, fmt.Errorf("unsupported driver: %q", driverName)
	}
}

// Tx returns a new transactional client. The provided context
// is used until the transaction is committed or rolled back.
func (c *Client) Tx(ctx context.Context) (*Tx, error) {
	if _, ok := c.driver.(*txDriver); ok {
		return nil, errors.New("ent: cannot start a transaction within a transaction")
	}
	tx, err := newTx(ctx, c.driver)
	if err != nil {
		return nil, fmt.Errorf("ent: starting a transaction: %w", err)
	}
	cfg := c.config
	cfg.driver = tx
	return &Tx{
		ctx:    ctx,
		config: cfg,
		Exam:   NewExamClient(cfg),
	}, nil
}

// BeginTx returns a transactional client with specified options.
func (c *Client) BeginTx(ctx context.Context, opts *sql.TxOptions) (*Tx, error) {
	if _, ok := c.driver.(*txDriver); ok {
		return nil, errors.New("ent: cannot start a transaction within a transaction")
	}
	tx, err := c.driver.(interface {
		BeginTx(context.Context, *sql.TxOptions) (dialect.Tx, error)
	}).BeginTx(ctx, opts)
	if err != nil {
		return nil, fmt.Errorf("ent: starting a transaction: %w", err)
	}
	cfg := c.config
	cfg.driver = &txDriver{tx: tx, drv: c.driver}
	return &Tx{
		ctx:    ctx,
		config: cfg,
		Exam:   NewExamClient(cfg),
	}, nil
}

// Debug returns a new debug-client. It's used to get verbose logging on specific operations.
//
//	client.Debug().
//		Exam.
//		Query().
//		Count(ctx)
func (c *Client) Debug() *Client {
	if c.debug {
		return c
	}
	cfg := c.config
	cfg.driver = dialect.Debug(c.driver, c.log)
	client := &Client{config: cfg}
	client.init()
	return client
}

// Close closes the database connection and prevents new queries from starting.
func (c *Client) Close() error {
	return c.driver.Close()
}

// Use adds the mutation hooks to all the entity clients.
// In order to add hooks to a specific client, call: `client.Node.Use(...)`.
func (c *Client) Use(hooks ...Hook) {
	c.Exam.Use(hooks...)
}

// Intercept adds the query interceptors to all the entity clients.
// In order to add interceptors to a specific client, call: `client.Node.Intercept(...)`.
func (c *Client) Intercept(interceptors ...Interceptor) {
	c.Exam.Intercept(interceptors...)
}

// Mutate implements the ent.Mutator interface.
func (c *Client) Mutate(ctx context.Context, m Mutation) (Value, error) {
	switch m := m.(type) {
	case *ExamMutation:
		return c.Exam.mutate(ctx, m)
	default:
		return nil, fmt.Errorf("ent: unknown mutation type %T", m)
	}
}

// ExamClient is a client for the Exam schema.
type ExamClient struct {
	config
}

// NewExamClient returns a client for the Exam from the given config.
func NewExamClient(c config) *ExamClient {
	return &ExamClient{config: c}
}

// Use adds a list of mutation hooks to the hooks stack.
// A call to `Use(f, g, h)` equals to `exam.Hooks(f(g(h())))`.
func (c *ExamClient) Use(hooks ...Hook) {
	c.hooks.Exam = append(c.hooks.Exam, hooks...)
}

// Intercept adds a list of query interceptors to the interceptors stack.
// A call to `Intercept(f, g, h)` equals to `exam.Intercept(f(g(h())))`.
func (c *ExamClient) Intercept(interceptors ...Interceptor) {
	c.inters.Exam = append(c.inters.Exam, interceptors...)
}

// Create returns a builder for creating a Exam entity.
func (c *ExamClient) Create() *ExamCreate {
	mutation := newExamMutation(c.config, OpCreate)
	return &ExamCreate{config: c.config, hooks: c.Hooks(), mutation: mutation}
}

// CreateBulk returns a builder for creating a bulk of Exam entities.
func (c *ExamClient) CreateBulk(builders ...*ExamCreate) *ExamCreateBulk {
	return &ExamCreateBulk{config: c.config, builders: builders}
}

// Update returns an update builder for Exam.
func (c *ExamClient) Update() *ExamUpdate {
	mutation := newExamMutation(c.config, OpUpdate)
	return &ExamUpdate{config: c.config, hooks: c.Hooks(), mutation: mutation}
}

// UpdateOne returns an update builder for the given entity.
func (c *ExamClient) UpdateOne(e *Exam) *ExamUpdateOne {
	mutation := newExamMutation(c.config, OpUpdateOne, withExam(e))
	return &ExamUpdateOne{config: c.config, hooks: c.Hooks(), mutation: mutation}
}

// UpdateOneID returns an update builder for the given id.
func (c *ExamClient) UpdateOneID(id int) *ExamUpdateOne {
	mutation := newExamMutation(c.config, OpUpdateOne, withExamID(id))
	return &ExamUpdateOne{config: c.config, hooks: c.Hooks(), mutation: mutation}
}

// Delete returns a delete builder for Exam.
func (c *ExamClient) Delete() *ExamDelete {
	mutation := newExamMutation(c.config, OpDelete)
	return &ExamDelete{config: c.config, hooks: c.Hooks(), mutation: mutation}
}

// DeleteOne returns a builder for deleting the given entity.
func (c *ExamClient) DeleteOne(e *Exam) *ExamDeleteOne {
	return c.DeleteOneID(e.ID)
}

// DeleteOneID returns a builder for deleting the given entity by its id.
func (c *ExamClient) DeleteOneID(id int) *ExamDeleteOne {
	builder := c.Delete().Where(exam.ID(id))
	builder.mutation.id = &id
	builder.mutation.op = OpDeleteOne
	return &ExamDeleteOne{builder}
}

// Query returns a query builder for Exam.
func (c *ExamClient) Query() *ExamQuery {
	return &ExamQuery{
		config: c.config,
		ctx:    &QueryContext{Type: TypeExam},
		inters: c.Interceptors(),
	}
}

// Get returns a Exam entity by its id.
func (c *ExamClient) Get(ctx context.Context, id int) (*Exam, error) {
	return c.Query().Where(exam.ID(id)).Only(ctx)
}

// GetX is like Get, but panics if an error occurs.
func (c *ExamClient) GetX(ctx context.Context, id int) *Exam {
	obj, err := c.Get(ctx, id)
	if err != nil {
		panic(err)
	}
	return obj
}

// Hooks returns the client hooks.
func (c *ExamClient) Hooks() []Hook {
	return c.hooks.Exam
}

// Interceptors returns the client interceptors.
func (c *ExamClient) Interceptors() []Interceptor {
	return c.inters.Exam
}

func (c *ExamClient) mutate(ctx context.Context, m *ExamMutation) (Value, error) {
	switch m.Op() {
	case OpCreate:
		return (&ExamCreate{config: c.config, hooks: c.Hooks(), mutation: m}).Save(ctx)
	case OpUpdate:
		return (&ExamUpdate{config: c.config, hooks: c.Hooks(), mutation: m}).Save(ctx)
	case OpUpdateOne:
		return (&ExamUpdateOne{config: c.config, hooks: c.Hooks(), mutation: m}).Save(ctx)
	case OpDelete, OpDeleteOne:
		return (&ExamDelete{config: c.config, hooks: c.Hooks(), mutation: m}).Exec(ctx)
	default:
		return nil, fmt.Errorf("ent: unknown Exam mutation op: %q", m.Op())
	}
}

// hooks and interceptors per client, for fast access.
type (
	hooks struct {
		Exam []ent.Hook
	}
	inters struct {
		Exam []ent.Interceptor
	}
)