// Code generated by ent, DO NOT EDIT.

package exam

import (
	"lms-class/ent/predicate"
	"time"

	"entgo.io/ent/dialect/sql"
)

// ID filters vertices based on their ID field.
func ID(id int) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldID, id))
}

// IDEQ applies the EQ predicate on the ID field.
func IDEQ(id int) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldID, id))
}

// IDNEQ applies the NEQ predicate on the ID field.
func IDNEQ(id int) predicate.Exam {
	return predicate.Exam(sql.FieldNEQ(FieldID, id))
}

// IDIn applies the In predicate on the ID field.
func IDIn(ids ...int) predicate.Exam {
	return predicate.Exam(sql.FieldIn(FieldID, ids...))
}

// IDNotIn applies the NotIn predicate on the ID field.
func IDNotIn(ids ...int) predicate.Exam {
	return predicate.Exam(sql.FieldNotIn(FieldID, ids...))
}

// IDGT applies the GT predicate on the ID field.
func IDGT(id int) predicate.Exam {
	return predicate.Exam(sql.FieldGT(FieldID, id))
}

// IDGTE applies the GTE predicate on the ID field.
func IDGTE(id int) predicate.Exam {
	return predicate.Exam(sql.FieldGTE(FieldID, id))
}

// IDLT applies the LT predicate on the ID field.
func IDLT(id int) predicate.Exam {
	return predicate.Exam(sql.FieldLT(FieldID, id))
}

// IDLTE applies the LTE predicate on the ID field.
func IDLTE(id int) predicate.Exam {
	return predicate.Exam(sql.FieldLTE(FieldID, id))
}

// Title applies equality check predicate on the "title" field. It's identical to TitleEQ.
func Title(v string) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldTitle, v))
}

// Context applies equality check predicate on the "context" field. It's identical to ContextEQ.
func Context(v string) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldContext, v))
}

// ContextId applies equality check predicate on the "contextId" field. It's identical to ContextIdEQ.
func ContextId(v string) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldContextId, v))
}

// IsPublished applies equality check predicate on the "isPublished" field. It's identical to IsPublishedEQ.
func IsPublished(v bool) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldIsPublished, v))
}

// UpdatedAt applies equality check predicate on the "updatedAt" field. It's identical to UpdatedAtEQ.
func UpdatedAt(v time.Time) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldUpdatedAt, v))
}

// TitleEQ applies the EQ predicate on the "title" field.
func TitleEQ(v string) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldTitle, v))
}

// TitleNEQ applies the NEQ predicate on the "title" field.
func TitleNEQ(v string) predicate.Exam {
	return predicate.Exam(sql.FieldNEQ(FieldTitle, v))
}

// TitleIn applies the In predicate on the "title" field.
func TitleIn(vs ...string) predicate.Exam {
	return predicate.Exam(sql.FieldIn(FieldTitle, vs...))
}

// TitleNotIn applies the NotIn predicate on the "title" field.
func TitleNotIn(vs ...string) predicate.Exam {
	return predicate.Exam(sql.FieldNotIn(FieldTitle, vs...))
}

// TitleGT applies the GT predicate on the "title" field.
func TitleGT(v string) predicate.Exam {
	return predicate.Exam(sql.FieldGT(FieldTitle, v))
}

// TitleGTE applies the GTE predicate on the "title" field.
func TitleGTE(v string) predicate.Exam {
	return predicate.Exam(sql.FieldGTE(FieldTitle, v))
}

// TitleLT applies the LT predicate on the "title" field.
func TitleLT(v string) predicate.Exam {
	return predicate.Exam(sql.FieldLT(FieldTitle, v))
}

// TitleLTE applies the LTE predicate on the "title" field.
func TitleLTE(v string) predicate.Exam {
	return predicate.Exam(sql.FieldLTE(FieldTitle, v))
}

// TitleContains applies the Contains predicate on the "title" field.
func TitleContains(v string) predicate.Exam {
	return predicate.Exam(sql.FieldContains(FieldTitle, v))
}

// TitleHasPrefix applies the HasPrefix predicate on the "title" field.
func TitleHasPrefix(v string) predicate.Exam {
	return predicate.Exam(sql.FieldHasPrefix(FieldTitle, v))
}

// TitleHasSuffix applies the HasSuffix predicate on the "title" field.
func TitleHasSuffix(v string) predicate.Exam {
	return predicate.Exam(sql.FieldHasSuffix(FieldTitle, v))
}

// TitleEqualFold applies the EqualFold predicate on the "title" field.
func TitleEqualFold(v string) predicate.Exam {
	return predicate.Exam(sql.FieldEqualFold(FieldTitle, v))
}

// TitleContainsFold applies the ContainsFold predicate on the "title" field.
func TitleContainsFold(v string) predicate.Exam {
	return predicate.Exam(sql.FieldContainsFold(FieldTitle, v))
}

// ContextEQ applies the EQ predicate on the "context" field.
func ContextEQ(v string) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldContext, v))
}

// ContextNEQ applies the NEQ predicate on the "context" field.
func ContextNEQ(v string) predicate.Exam {
	return predicate.Exam(sql.FieldNEQ(FieldContext, v))
}

// ContextIn applies the In predicate on the "context" field.
func ContextIn(vs ...string) predicate.Exam {
	return predicate.Exam(sql.FieldIn(FieldContext, vs...))
}

// ContextNotIn applies the NotIn predicate on the "context" field.
func ContextNotIn(vs ...string) predicate.Exam {
	return predicate.Exam(sql.FieldNotIn(FieldContext, vs...))
}

// ContextGT applies the GT predicate on the "context" field.
func ContextGT(v string) predicate.Exam {
	return predicate.Exam(sql.FieldGT(FieldContext, v))
}

// ContextGTE applies the GTE predicate on the "context" field.
func ContextGTE(v string) predicate.Exam {
	return predicate.Exam(sql.FieldGTE(FieldContext, v))
}

// ContextLT applies the LT predicate on the "context" field.
func ContextLT(v string) predicate.Exam {
	return predicate.Exam(sql.FieldLT(FieldContext, v))
}

// ContextLTE applies the LTE predicate on the "context" field.
func ContextLTE(v string) predicate.Exam {
	return predicate.Exam(sql.FieldLTE(FieldContext, v))
}

// ContextContains applies the Contains predicate on the "context" field.
func ContextContains(v string) predicate.Exam {
	return predicate.Exam(sql.FieldContains(FieldContext, v))
}

// ContextHasPrefix applies the HasPrefix predicate on the "context" field.
func ContextHasPrefix(v string) predicate.Exam {
	return predicate.Exam(sql.FieldHasPrefix(FieldContext, v))
}

// ContextHasSuffix applies the HasSuffix predicate on the "context" field.
func ContextHasSuffix(v string) predicate.Exam {
	return predicate.Exam(sql.FieldHasSuffix(FieldContext, v))
}

// ContextEqualFold applies the EqualFold predicate on the "context" field.
func ContextEqualFold(v string) predicate.Exam {
	return predicate.Exam(sql.FieldEqualFold(FieldContext, v))
}

// ContextContainsFold applies the ContainsFold predicate on the "context" field.
func ContextContainsFold(v string) predicate.Exam {
	return predicate.Exam(sql.FieldContainsFold(FieldContext, v))
}

// ContextIdEQ applies the EQ predicate on the "contextId" field.
func ContextIdEQ(v string) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldContextId, v))
}

// ContextIdNEQ applies the NEQ predicate on the "contextId" field.
func ContextIdNEQ(v string) predicate.Exam {
	return predicate.Exam(sql.FieldNEQ(FieldContextId, v))
}

// ContextIdIn applies the In predicate on the "contextId" field.
func ContextIdIn(vs ...string) predicate.Exam {
	return predicate.Exam(sql.FieldIn(FieldContextId, vs...))
}

// ContextIdNotIn applies the NotIn predicate on the "contextId" field.
func ContextIdNotIn(vs ...string) predicate.Exam {
	return predicate.Exam(sql.FieldNotIn(FieldContextId, vs...))
}

// ContextIdGT applies the GT predicate on the "contextId" field.
func ContextIdGT(v string) predicate.Exam {
	return predicate.Exam(sql.FieldGT(FieldContextId, v))
}

// ContextIdGTE applies the GTE predicate on the "contextId" field.
func ContextIdGTE(v string) predicate.Exam {
	return predicate.Exam(sql.FieldGTE(FieldContextId, v))
}

// ContextIdLT applies the LT predicate on the "contextId" field.
func ContextIdLT(v string) predicate.Exam {
	return predicate.Exam(sql.FieldLT(FieldContextId, v))
}

// ContextIdLTE applies the LTE predicate on the "contextId" field.
func ContextIdLTE(v string) predicate.Exam {
	return predicate.Exam(sql.FieldLTE(FieldContextId, v))
}

// ContextIdContains applies the Contains predicate on the "contextId" field.
func ContextIdContains(v string) predicate.Exam {
	return predicate.Exam(sql.FieldContains(FieldContextId, v))
}

// ContextIdHasPrefix applies the HasPrefix predicate on the "contextId" field.
func ContextIdHasPrefix(v string) predicate.Exam {
	return predicate.Exam(sql.FieldHasPrefix(FieldContextId, v))
}

// ContextIdHasSuffix applies the HasSuffix predicate on the "contextId" field.
func ContextIdHasSuffix(v string) predicate.Exam {
	return predicate.Exam(sql.FieldHasSuffix(FieldContextId, v))
}

// ContextIdEqualFold applies the EqualFold predicate on the "contextId" field.
func ContextIdEqualFold(v string) predicate.Exam {
	return predicate.Exam(sql.FieldEqualFold(FieldContextId, v))
}

// ContextIdContainsFold applies the ContainsFold predicate on the "contextId" field.
func ContextIdContainsFold(v string) predicate.Exam {
	return predicate.Exam(sql.FieldContainsFold(FieldContextId, v))
}

// IsPublishedEQ applies the EQ predicate on the "isPublished" field.
func IsPublishedEQ(v bool) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldIsPublished, v))
}

// IsPublishedNEQ applies the NEQ predicate on the "isPublished" field.
func IsPublishedNEQ(v bool) predicate.Exam {
	return predicate.Exam(sql.FieldNEQ(FieldIsPublished, v))
}

// UpdatedAtEQ applies the EQ predicate on the "updatedAt" field.
func UpdatedAtEQ(v time.Time) predicate.Exam {
	return predicate.Exam(sql.FieldEQ(FieldUpdatedAt, v))
}

// UpdatedAtNEQ applies the NEQ predicate on the "updatedAt" field.
func UpdatedAtNEQ(v time.Time) predicate.Exam {
	return predicate.Exam(sql.FieldNEQ(FieldUpdatedAt, v))
}

// UpdatedAtIn applies the In predicate on the "updatedAt" field.
func UpdatedAtIn(vs ...time.Time) predicate.Exam {
	return predicate.Exam(sql.FieldIn(FieldUpdatedAt, vs...))
}

// UpdatedAtNotIn applies the NotIn predicate on the "updatedAt" field.
func UpdatedAtNotIn(vs ...time.Time) predicate.Exam {
	return predicate.Exam(sql.FieldNotIn(FieldUpdatedAt, vs...))
}

// UpdatedAtGT applies the GT predicate on the "updatedAt" field.
func UpdatedAtGT(v time.Time) predicate.Exam {
	return predicate.Exam(sql.FieldGT(FieldUpdatedAt, v))
}

// UpdatedAtGTE applies the GTE predicate on the "updatedAt" field.
func UpdatedAtGTE(v time.Time) predicate.Exam {
	return predicate.Exam(sql.FieldGTE(FieldUpdatedAt, v))
}

// UpdatedAtLT applies the LT predicate on the "updatedAt" field.
func UpdatedAtLT(v time.Time) predicate.Exam {
	return predicate.Exam(sql.FieldLT(FieldUpdatedAt, v))
}

// UpdatedAtLTE applies the LTE predicate on the "updatedAt" field.
func UpdatedAtLTE(v time.Time) predicate.Exam {
	return predicate.Exam(sql.FieldLTE(FieldUpdatedAt, v))
}

// And groups predicates with the AND operator between them.
func And(predicates ...predicate.Exam) predicate.Exam {
	return predicate.Exam(func(s *sql.Selector) {
		s1 := s.Clone().SetP(nil)
		for _, p := range predicates {
			p(s1)
		}
		s.Where(s1.P())
	})
}

// Or groups predicates with the OR operator between them.
func Or(predicates ...predicate.Exam) predicate.Exam {
	return predicate.Exam(func(s *sql.Selector) {
		s1 := s.Clone().SetP(nil)
		for i, p := range predicates {
			if i > 0 {
				s1.Or()
			}
			p(s1)
		}
		s.Where(s1.P())
	})
}

// Not applies the not operator on the given predicate.
func Not(p predicate.Exam) predicate.Exam {
	return predicate.Exam(func(s *sql.Selector) {
		p(s.Not())
	})
}
