// Code generated by enthistory, DO NOT EDIT.
// Code generated by ent, DO NOT EDIT.

package ent

import (
	"context"
	"time"

	"lms-class/ent/examhistory"
	"lms-class/ent/questionhistory"

	"entgo.io/ent/dialect/sql"
)

func (e *Exam) History() *ExamHistoryQuery {
	historyClient := NewExamHistoryClient(e.config)
	return historyClient.Query().Where(examhistory.Ref(e.ID))
}

func (eh *ExamHistory) Next(ctx context.Context) (*ExamHistory, error) {
	client := NewExamHistoryClient(eh.config)
	return client.Query().
		Where(
			examhistory.Ref(eh.Ref),
			examhistory.HistoryTimeGT(eh.HistoryTime),
		).
		Order(examhistory.ByHistoryTime()).
		First(ctx)
}

func (eh *ExamHistory) Prev(ctx context.Context) (*ExamHistory, error) {
	client := NewExamHistoryClient(eh.config)
	return client.Query().
		Where(
			examhistory.Ref(eh.Ref),
			examhistory.HistoryTimeLT(eh.HistoryTime),
		).
		Order(examhistory.ByHistoryTime(sql.OrderDesc())).
		First(ctx)
}

func (ehq *ExamHistoryQuery) Earliest(ctx context.Context) (*ExamHistory, error) {
	return ehq.
		Order(examhistory.ByHistoryTime()).
		First(ctx)
}

func (ehq *ExamHistoryQuery) Latest(ctx context.Context) (*ExamHistory, error) {
	return ehq.
		Order(examhistory.ByHistoryTime(sql.OrderDesc())).
		First(ctx)
}

func (ehq *ExamHistoryQuery) AsOf(ctx context.Context, time time.Time) (*ExamHistory, error) {
	return ehq.
		Where(examhistory.HistoryTimeLTE(time)).
		Order(examhistory.ByHistoryTime(sql.OrderDesc())).
		First(ctx)
}

func (eh *ExamHistory) Restore(ctx context.Context) (*Exam, error) {
	client := NewExamClient(eh.config)
	return client.
		UpdateOneID(eh.Ref).
		SetTitle(eh.Title).
		SetContext(eh.Context).
		SetContextId(eh.ContextId).
		SetIsPublished(eh.IsPublished).
		SetHavingDraft(eh.HavingDraft).
		SetNillableLastPublishedAt(eh.LastPublishedAt).
		SetUpdatedAt(eh.UpdatedAt).
		Save(ctx)
}

func (q *Question) History() *QuestionHistoryQuery {
	historyClient := NewQuestionHistoryClient(q.config)
	return historyClient.Query().Where(questionhistory.Ref(q.ID))
}

func (qh *QuestionHistory) Next(ctx context.Context) (*QuestionHistory, error) {
	client := NewQuestionHistoryClient(qh.config)
	return client.Query().
		Where(
			questionhistory.Ref(qh.Ref),
			questionhistory.HistoryTimeGT(qh.HistoryTime),
		).
		Order(questionhistory.ByHistoryTime()).
		First(ctx)
}

func (qh *QuestionHistory) Prev(ctx context.Context) (*QuestionHistory, error) {
	client := NewQuestionHistoryClient(qh.config)
	return client.Query().
		Where(
			questionhistory.Ref(qh.Ref),
			questionhistory.HistoryTimeLT(qh.HistoryTime),
		).
		Order(questionhistory.ByHistoryTime(sql.OrderDesc())).
		First(ctx)
}

func (qhq *QuestionHistoryQuery) Earliest(ctx context.Context) (*QuestionHistory, error) {
	return qhq.
		Order(questionhistory.ByHistoryTime()).
		First(ctx)
}

func (qhq *QuestionHistoryQuery) Latest(ctx context.Context) (*QuestionHistory, error) {
	return qhq.
		Order(questionhistory.ByHistoryTime(sql.OrderDesc())).
		First(ctx)
}

func (qhq *QuestionHistoryQuery) AsOf(ctx context.Context, time time.Time) (*QuestionHistory, error) {
	return qhq.
		Where(questionhistory.HistoryTimeLTE(time)).
		Order(questionhistory.ByHistoryTime(sql.OrderDesc())).
		First(ctx)
}

func (qh *QuestionHistory) Restore(ctx context.Context) (*Question, error) {
	client := NewQuestionClient(qh.config)
	return client.
		UpdateOneID(qh.Ref).
		SetContext(qh.Context).
		SetContextId(qh.ContextId).
		SetPosition(qh.Position).
		SetQuestionType(qh.QuestionType).
		SetData(qh.Data).
		SetUpdatedAt(qh.UpdatedAt).
		SetVersion(qh.Version).
		Save(ctx)
}
