// Code generated by enthistory, DO NOT EDIT.

// Code generated by ent, DO NOT EDIT.

package ent

import (
	"context"
	"errors"
	"fmt"
	"time"

	"entgo.io/ent"
	"github.com/flume/enthistory"
)

var (
	idNotFoundError = errors.New("could not get id from mutation")
)

func EntOpToHistoryOp(op ent.Op) enthistory.OpType {
	switch op {
	case ent.OpDelete, ent.OpDeleteOne:
		return enthistory.OpTypeDelete
	case ent.OpUpdate, ent.OpUpdateOne:
		return enthistory.OpTypeUpdate
	default:
		return enthistory.OpTypeInsert
	}
}

func rollback(tx *Tx, err error) error {
	if tx != nil {
		if rerr := tx.Rollback(); rerr != nil {
			err = fmt.Errorf("%w: %v", err, rerr)
		}
		return err
	}
	return err
}

func (m *ExamMutation) CreateHistoryFromCreate(ctx context.Context) error {
	client := m.Client()
	tx, err := m.Tx()
	if err != nil {
		tx = nil
	}

	updatedBy, _ := ctx.Value("userId").(int)

	id, ok := m.ID()
	if !ok {
		return rollback(tx, idNotFoundError)
	}

	create := client.ExamHistory.Create()
	if tx != nil {
		create = tx.ExamHistory.Create()
	}
	create = create.
		SetOperation(EntOpToHistoryOp(m.Op())).
		SetHistoryTime(time.Now()).
		SetRef(id)
	if updatedBy != 0 {
		create = create.SetUpdatedBy(updatedBy)
	}

	if title, exists := m.Title(); exists {
		create = create.SetTitle(title)
	}

	if context, exists := m.Context(); exists {
		create = create.SetContext(context)
	}

	if contextid, exists := m.ContextId(); exists {
		create = create.SetContextId(contextid)
	}

	if ispublished, exists := m.IsPublished(); exists {
		create = create.SetIsPublished(ispublished)
	}

	if havingdraft, exists := m.HavingDraft(); exists {
		create = create.SetHavingDraft(havingdraft)
	}

	if lastpublishedat, exists := m.LastPublishedAt(); exists {
		create = create.SetNillableLastPublishedAt(&lastpublishedat)
	}

	if updatedat, exists := m.UpdatedAt(); exists {
		create = create.SetUpdatedAt(updatedat)
	}

	_, err = create.Save(ctx)
	if err != nil {
		rollback(tx, err)
	}
	return nil
}

func (m *ExamMutation) CreateHistoryFromUpdate(ctx context.Context) error {
	client := m.Client()
	tx, err := m.Tx()
	if err != nil {
		tx = nil
	}

	updatedBy, _ := ctx.Value("userId").(int)

	id, ok := m.ID()
	if !ok {
		return rollback(tx, idNotFoundError)
	}

	exam, err := client.Exam.Get(ctx, id)
	if err != nil {
		return rollback(tx, err)
	}

	create := client.ExamHistory.Create()
	if tx != nil {
		create = tx.ExamHistory.Create()
	}
	create = create.
		SetOperation(EntOpToHistoryOp(m.Op())).
		SetHistoryTime(time.Now()).
		SetRef(id)
	if updatedBy != 0 {
		create = create.SetUpdatedBy(updatedBy)
	}

	if title, exists := m.Title(); exists {
		create = create.SetTitle(title)
	} else {
		create = create.SetTitle(exam.Title)
	}

	if context, exists := m.Context(); exists {
		create = create.SetContext(context)
	} else {
		create = create.SetContext(exam.Context)
	}

	if contextid, exists := m.ContextId(); exists {
		create = create.SetContextId(contextid)
	} else {
		create = create.SetContextId(exam.ContextId)
	}

	if ispublished, exists := m.IsPublished(); exists {
		create = create.SetIsPublished(ispublished)
	} else {
		create = create.SetIsPublished(exam.IsPublished)
	}

	if havingdraft, exists := m.HavingDraft(); exists {
		create = create.SetHavingDraft(havingdraft)
	} else {
		create = create.SetHavingDraft(exam.HavingDraft)
	}

	if lastpublishedat, exists := m.LastPublishedAt(); exists {
		create = create.SetNillableLastPublishedAt(&lastpublishedat)
	} else {
		create = create.SetNillableLastPublishedAt(exam.LastPublishedAt)
	}

	if updatedat, exists := m.UpdatedAt(); exists {
		create = create.SetUpdatedAt(updatedat)
	} else {
		create = create.SetUpdatedAt(exam.UpdatedAt)
	}

	_, err = create.Save(ctx)
	if err != nil {
		rollback(tx, err)
	}
	return nil
}

func (m *ExamMutation) CreateHistoryFromDelete(ctx context.Context) error {
	client := m.Client()
	tx, err := m.Tx()
	if err != nil {
		tx = nil
	}

	updatedBy, _ := ctx.Value("userId").(int)

	id, ok := m.ID()
	if !ok {
		return rollback(tx, idNotFoundError)
	}

	exam, err := client.Exam.Get(ctx, id)
	if err != nil {
		return rollback(tx, err)
	}

	create := client.ExamHistory.Create()
	if tx != nil {
		create = tx.ExamHistory.Create()
	}
	if updatedBy != 0 {
		create = create.SetUpdatedBy(updatedBy)
	}

	_, err = create.
		SetOperation(EntOpToHistoryOp(m.Op())).
		SetHistoryTime(time.Now()).
		SetRef(id).
		SetTitle(exam.Title).
		SetContext(exam.Context).
		SetContextId(exam.ContextId).
		SetIsPublished(exam.IsPublished).
		SetHavingDraft(exam.HavingDraft).
		SetNillableLastPublishedAt(exam.LastPublishedAt).
		SetUpdatedAt(exam.UpdatedAt).
		Save(ctx)
	if err != nil {
		rollback(tx, err)
	}
	return nil
}

func (m *QuestionMutation) CreateHistoryFromCreate(ctx context.Context) error {
	client := m.Client()
	tx, err := m.Tx()
	if err != nil {
		tx = nil
	}

	updatedBy, _ := ctx.Value("userId").(int)

	id, ok := m.ID()
	if !ok {
		return rollback(tx, idNotFoundError)
	}

	create := client.QuestionHistory.Create()
	if tx != nil {
		create = tx.QuestionHistory.Create()
	}
	create = create.
		SetOperation(EntOpToHistoryOp(m.Op())).
		SetHistoryTime(time.Now()).
		SetRef(id)
	if updatedBy != 0 {
		create = create.SetUpdatedBy(updatedBy)
	}

	if context, exists := m.Context(); exists {
		create = create.SetContext(context)
	}

	if contextid, exists := m.ContextId(); exists {
		create = create.SetContextId(contextid)
	}

	if position, exists := m.Position(); exists {
		create = create.SetPosition(position)
	}

	if questiontype, exists := m.QuestionType(); exists {
		create = create.SetQuestionType(questiontype)
	}

	if data, exists := m.Data(); exists {
		create = create.SetData(data)
	}

	if updatedat, exists := m.UpdatedAt(); exists {
		create = create.SetUpdatedAt(updatedat)
	}

	_, err = create.Save(ctx)
	if err != nil {
		rollback(tx, err)
	}
	return nil
}

func (m *QuestionMutation) CreateHistoryFromUpdate(ctx context.Context) error {
	client := m.Client()
	tx, err := m.Tx()
	if err != nil {
		tx = nil
	}

	updatedBy, _ := ctx.Value("userId").(int)

	id, ok := m.ID()
	if !ok {
		return rollback(tx, idNotFoundError)
	}

	question, err := client.Question.Get(ctx, id)
	if err != nil {
		return rollback(tx, err)
	}

	create := client.QuestionHistory.Create()
	if tx != nil {
		create = tx.QuestionHistory.Create()
	}
	create = create.
		SetOperation(EntOpToHistoryOp(m.Op())).
		SetHistoryTime(time.Now()).
		SetRef(id)
	if updatedBy != 0 {
		create = create.SetUpdatedBy(updatedBy)
	}

	if context, exists := m.Context(); exists {
		create = create.SetContext(context)
	} else {
		create = create.SetContext(question.Context)
	}

	if contextid, exists := m.ContextId(); exists {
		create = create.SetContextId(contextid)
	} else {
		create = create.SetContextId(question.ContextId)
	}

	if position, exists := m.Position(); exists {
		create = create.SetPosition(position)
	} else {
		create = create.SetPosition(question.Position)
	}

	if questiontype, exists := m.QuestionType(); exists {
		create = create.SetQuestionType(questiontype)
	} else {
		create = create.SetQuestionType(question.QuestionType)
	}

	if data, exists := m.Data(); exists {
		create = create.SetData(data)
	} else {
		create = create.SetData(question.Data)
	}

	if updatedat, exists := m.UpdatedAt(); exists {
		create = create.SetUpdatedAt(updatedat)
	} else {
		create = create.SetUpdatedAt(question.UpdatedAt)
	}

	_, err = create.Save(ctx)
	if err != nil {
		rollback(tx, err)
	}
	return nil
}

func (m *QuestionMutation) CreateHistoryFromDelete(ctx context.Context) error {
	client := m.Client()
	tx, err := m.Tx()
	if err != nil {
		tx = nil
	}

	updatedBy, _ := ctx.Value("userId").(int)

	id, ok := m.ID()
	if !ok {
		return rollback(tx, idNotFoundError)
	}

	question, err := client.Question.Get(ctx, id)
	if err != nil {
		return rollback(tx, err)
	}

	create := client.QuestionHistory.Create()
	if tx != nil {
		create = tx.QuestionHistory.Create()
	}
	if updatedBy != 0 {
		create = create.SetUpdatedBy(updatedBy)
	}

	_, err = create.
		SetOperation(EntOpToHistoryOp(m.Op())).
		SetHistoryTime(time.Now()).
		SetRef(id).
		SetContext(question.Context).
		SetContextId(question.ContextId).
		SetPosition(question.Position).
		SetQuestionType(question.QuestionType).
		SetData(question.Data).
		SetUpdatedAt(question.UpdatedAt).
		Save(ctx)
	if err != nil {
		rollback(tx, err)
	}
	return nil
}
