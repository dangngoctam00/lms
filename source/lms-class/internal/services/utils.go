package services

import (
	"context"
	"fmt"
	"lms-class/ent"
	"log"
)

func Commit(tx *ent.Tx) {
	if err := recover(); err != nil {
		if err := tx.Rollback(); err != nil {
			log.Println("Error while trying roll back exception: ", err)
		}
	} else if err := tx.Commit(); err != nil {
		log.Println("Error while trying commit transaction: ", err)
	}
}

func getZero[T any]() T {
	var result T
	return result
}

func WithTx[T any](ctx context.Context, client *ent.Client, fn func(tx *ent.Tx) (T, error)) (T, error) {
	tx, err := client.Tx(ctx)
	if err != nil {
		return getZero[T](), err
	}
	defer func() {
		if v := recover(); v != nil {
			err := tx.Rollback()
			if err != nil {
				log.Println("unexpected error: ", err)
				return
			}
			panic(v)
		}
	}()
	res, err := fn(tx)
	if err != nil {
		if rErr := tx.Rollback(); rErr != nil {
			err = fmt.Errorf("%w: rolling back transaction: %v", err, rErr)
		}
		return getZero[T](), err
	}
	if err := tx.Commit(); err != nil {
		return getZero[T](), fmt.Errorf("committing transaction: %w", err)
	}
	return res, nil
}

func OpenTx(ctx context.Context, c *ent.Client) (context.Context, *ent.Tx, error) {
	// Return a new context holds a *TxInfo to be updated by the
	// callbacks above.
	//ctx = context.WithValue(ctx, ctxKey{}, &TxInfo{})
	tx, err := c.Tx(ctx)
	if err != nil {
		return nil, nil, err
	}
	// Add a hook on Tx.Commit.
	tx.OnCommit(func(next ent.Committer) ent.Committer {
		return ent.CommitFunc(func(ctx context.Context, tx *ent.Tx) error {
			// Code before the actual commit.
			err := next.Commit(ctx, tx)
			// Code after the transaction was committed.
			if err != nil {
				// Log failure.
				log.Println(err)
				// Store info in context-key.
				//ctx.Value(ctxKey{}).(*TxInfo).Err = err
			}
			return err
		})
	})
	// Add a hook on Tx.Rollback.
	tx.OnRollback(func(next ent.Rollbacker) ent.Rollbacker {
		return ent.RollbackFunc(func(ctx context.Context, tx *ent.Tx) error {
			err := next.Rollback(ctx, tx)
			if err != nil {
				// Log failure.
				log.Println(err)
				// Store info in context-key.
				//ctx.Value(ctxKey{}).(*TxInfo).Err = err
			}
			return err
		})
	})
	return ctx, tx, nil
}
