package services

import (
	"context"
	"encoding/json"
	"github.com/pkg/errors"
	"lms-class/common/xerr"
	"lms-class/ent"
	"lms-class/internal/queries"
	"lms-class/internal/web/dto"
	"lms-class/pkg/utils"
	"math/rand"
	"time"
)

func CreateQuiz(quizDto *dto.QuizDto) (*int, error) {
	return WithTx(context.Background(), utils.EntClient, func(tx *ent.Tx) (*int, error) {
		return doCreateQuiz(quizDto, tx)
	})
}

func GetQuizById(id int) (*ent.Quiz, error) {
	byId, err := queries.GetQuizById(id)
	if err != nil {
		if _, ok := err.(*ent.NotFoundError); ok {
			return nil, xerr.NewErrCodeAndInformation(xerr.ResourceNotFound, "quiz")
		}
		return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
	}
	return byId, err
}

func GetQuizSession(id int) (*dto.QuizSession, error) {
	byId, err := queries.GetQuizSubmissionById(id)
	if err != nil {
		if _, ok := err.(*ent.NotFoundError); ok {
			return nil, xerr.NewErrCodeAndInformation(xerr.ResourceNotFound, "quiz submission")
		}
		return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
	}
	response := new(dto.QuizSession)
	response.ID = byId.ID
	var questions []dto.QuestionQuizSession
	if err = json.Unmarshal(byId.Questions, &questions); err != nil {
		return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
	}
	for i, q := range questions {
		questions[i].ID = q.ID
		questions[i].QuestionType = q.QuestionType
		questions[i].Position = q.Position
		questions[i].SetData(q.QuestionType, q.Data)
		questions[i].Answers = byId.Answers[q.ID]
	}
	response.Questions = questions
	return response, nil
}

func AnswerQuestionById(sessionId, questionId int, answer []dto.Key) (*int, error) {
	return WithTx(context.Background(), utils.EntClient, func(tx *ent.Tx) (*int, error) {
		session, err := queries.GetQuizSubmissionByIdOnUpdate(tx, sessionId)
		if err != nil {
			if _, ok := err.(*ent.NotFoundError); ok {
				return nil, xerr.NewErrCodeAndInformation(xerr.ResourceNotFound, "quiz submission")
			}
			return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
		}
		quiz := session.Edges.Quiz
		if session.StartedAt.Add(time.Duration(*quiz.TimeLimit * int(time.Minute))).Before(time.Now()) {
			return nil, xerr.NewErrCodeAndInformation(xerr.QuizSessionClosed)
		}
		answers := session.Answers
		if answers == nil {
			answers = make(map[int][]dto.Key)
		}
		answers[questionId] = answer
		_, err = session.Update().
			SetAnswers(answers).
			Save(context.Background())
		if err != nil {
			return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
		}
		return &questionId, nil
	})
}

func DoQuiz(id int) (*int, error) {
	quizEntity, err := GetQuizById(id)
	if err != nil {
		return nil, err
	}
	if validateDoQuiz(quizEntity) == false {
		return nil, xerr.NewErrCodeAndInformation(xerr.QuizNotConfigured)
	}
	examId := quizEntity.ExamId
	exam, err := GetPublishedExam(*examId)
	if err != nil {
		return nil, err
	}
	questionsInSession := shuffleQuestions(exam)
	marshal, err := json.Marshal(questionsInSession)
	return WithTx(context.Background(), utils.EntClient, func(tx *ent.Tx) (*int, error) {
		save, err := tx.Client().QuizSubmission.Create().
			SetQuizId(id).
			SetUserId(1).
			SetStartedAt(time.Now()).
			SetQuestions(marshal).
			SetSubmittedAt(time.Now()).
			Save(context.Background())
		return &save.ID, err
	})
}

func validateDoQuiz(quiz *ent.Quiz) bool {
	return quiz.IsPublished == true &&
		quiz.ExamId != nil &&
		quiz.StartedAt != nil &&
		quiz.TimeLimit != nil
}

func shuffleQuestions(exam *dto.ExamDto) []dto.QuestionQuizSession {
	questions := exam.Questions
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	r.Shuffle(len(questions), func(i, j int) {
		questions[i], questions[j] = questions[j], questions[i]
	})
	for i := range questions {
		questions[i].Position = i
	}
	for i := range questions {
		if q, ok := questions[i].Data.(dto.QuestionAction); ok {
			q.Shuffle()
		}
	}
	questionsInSession := make([]dto.QuestionQuizSession, len(questions))
	for i, question := range questions {
		questionsInSession[i] = *dto.NewQuestionQuizSession(question)
	}
	return questionsInSession
}

func doCreateQuiz(quizDto *dto.QuizDto, tx *ent.Tx) (*int, error) {
	if quizDto.ExamId != nil {
		existed, err := IsExamExisted(*quizDto.ExamId)
		if err != nil {
			return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
		}
		if existed == false {
			return nil, xerr.NewErrCodeAndInformation(xerr.ResourceNotFound, "exam")
		}
	}
	now := time.Now()
	row, err := tx.Quiz.Create().
		SetTitle(quizDto.Title).
		SetDescription(quizDto.Description).
		SetGradeTag(quizDto.GradeTag).
		SetNillableExamId(quizDto.ExamId).
		SetIsPublished(quizDto.IsPublished).
		SetCreatedAt(now).
		SetUpdatedAt(now).
		SetContext(quizDto.Context).
		SetContextId(quizDto.ContextId).
		SetNillableParentId(quizDto.ParentId).
		SetNillableStartedAt(quizDto.StartedAt).
		SetNillableFinishedAt(quizDto.FinishedAt).
		SetNillableTimeLimit(quizDto.TimeLimit).
		SetNillableMaxAttempt(quizDto.MaxAttempt).
		SetViewPreviousSessions(quizDto.ViewPreviousSessions).
		SetNillableViewPreviousSessionsTime(quizDto.ViewPreviousSessionsTime).
		SetNillablePassedScore(quizDto.PassedScore).
		SetNillableFinalGradedStrategy(quizDto.FinalGradedStrategy).
		Save(context.Background())
	if err != nil {
		return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
	}
	return &row.ID, nil
}
