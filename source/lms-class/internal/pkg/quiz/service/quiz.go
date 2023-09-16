package service

import (
	"context"
	"encoding/json"
	"github.com/pkg/errors"
	"lms-class/common/xerr"
	"lms-class/ent"
	"lms-class/ent/quiz"
	"lms-class/internal/pkg/exam/dto"
	examService "lms-class/internal/pkg/exam/service"
	questionController "lms-class/internal/pkg/question/dto"
	quizController "lms-class/internal/pkg/quiz/dto"
	"lms-class/internal/pkg/quiz/query"
	"lms-class/internal/services"
	"lms-class/pkg/utils"
	"log"
	"math/rand"
	"time"
)

func SubmitQuizSession() {
	<-utils.DbCh
	// case user don't open frontend tab
	ticker := time.Tick(10 * time.Second)
	for {
		select {
		case <-ticker:
			sessions, err := query.GetUnSubmittedQuizSessions()
			if err != nil {
				log.Println(err)
				continue
			}
			log.Println("Number of un-submitted sessions: ", len(sessions))
		}
	}
}

func init() {
	go SubmitQuizSession()
}

func CreateQuiz(quizDto *quizController.QuizDto) (*int, error) {
	return services.WithTx(context.Background(), utils.EntClient, func(tx *ent.Tx) (*int, error) {
		return doCreateQuiz(quizDto, tx)
	})
}

func GetQuizById(id int) (*ent.Quiz, error) {
	byId, err := utils.EntClient.Quiz.
		Query().
		Where(quiz.ID(id)).
		Only(context.Background())
	if err != nil {
		if _, ok := err.(*ent.NotFoundError); ok {
			return nil, xerr.NewErrCodeAndInformation(xerr.ResourceNotFound, "quiz")
		}
		return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
	}
	return byId, err
}

func GetQuizSession(id int) (*quizController.QuizSession, error) {
	submission, err := query.GetQuizSubmissionById(id)
	if err != nil {
		if _, ok := err.(*ent.NotFoundError); ok {
			return nil, xerr.NewErrCodeAndInformation(xerr.ResourceNotFound, "quiz submission")
		}
		return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
	}
	quizId := submission.QuizId
	quizEntity, err := GetQuizById(quizId)
	if err != nil {
		return nil, err
	}
	if (quizEntity.ViewPreviousSessions != false && submission.SubmittedAt != nil) ||
		(quizEntity.ViewPreviousSessions && quizEntity.ViewPreviousSessionsTime != nil && quizEntity.ViewPreviousSessionsTime.After(time.Now())) {
		return nil, xerr.NewErrCode(xerr.QuizSessionIsNotAllowedView)
	}

	response := new(quizController.QuizSession)
	response.ID = submission.ID
	var questions []questionController.QuestionQuizSession
	if err = json.Unmarshal(submission.Questions, &questions); err != nil {
		return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
	}
	for i, q := range questions {
		questions[i].ID = q.ID
		questions[i].QuestionType = q.QuestionType
		questions[i].Position = q.Position
		err := questions[i].SetData(q.QuestionType, q.Data)
		if err != nil {
			return nil, err
		}
		questions[i].Answers = submission.Answers[q.ID]
	}
	if quizEntity.ViewResult == true && submission.SubmittedAt != nil {
		// TODO: fill result after graded
		return nil, nil
	}
	response.Questions = questions
	return response, nil
}

func AnswerQuestionById(sessionId, questionId int, answer []questionController.Key) (*int, error) {
	return services.WithTx(context.Background(), utils.EntClient, func(tx *ent.Tx) (*int, error) {
		session, err := query.GetQuizSubmissionByIdOnUpdate(tx, sessionId)
		if err != nil {
			if _, ok := err.(*ent.NotFoundError); ok {
				return nil, xerr.NewErrCodeAndInformation(xerr.ResourceNotFound, "quiz submission")
			}
			return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
		}
		quizEntity := session.Edges.Quiz
		if session.StartedAt.Add(time.Duration(*quizEntity.TimeLimit * int(time.Minute))).Before(time.Now()) {
			return nil, xerr.NewErrCodeAndInformation(xerr.QuizSessionClosed)
		}
		answers := session.Answers
		if answers == nil {
			answers = make(map[int][]questionController.Key)
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
	examEntity, err := examService.GetPublishedExam(*examId)
	if err != nil {
		return nil, err
	}
	questionsInSession := shuffleQuestions(examEntity)
	marshal, err := json.Marshal(questionsInSession)
	return services.WithTx(context.Background(), utils.EntClient, func(tx *ent.Tx) (*int, error) {
		save, err := tx.Client().QuizSubmission.Create().
			SetQuizId(id).
			SetUserId(1).
			SetStartedAt(time.Now()).
			SetQuestions(marshal).
			//SetSubmittedAt(time.Now()).
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

func shuffleQuestions(exam *dto.ExamDto) []questionController.QuestionQuizSession {
	questions := exam.Questions
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	r.Shuffle(len(questions), func(i, j int) {
		questions[i], questions[j] = questions[j], questions[i]
	})
	for i := range questions {
		questions[i].Position = i
	}
	for i := range questions {
		if q, ok := questions[i].Data.(questionController.IQuestion); ok {
			q.Shuffle()
		}
	}
	questionsInSession := make([]questionController.QuestionQuizSession, len(questions))
	for i, q := range questions {
		questionsInSession[i] = *questionController.NewQuestionQuizSession(q)
	}
	return questionsInSession
}

func doCreateQuiz(quizDto *quizController.QuizDto, tx *ent.Tx) (*int, error) {
	if quizDto.ExamId != nil {
		existed, err := examService.IsExamExisted(*quizDto.ExamId)
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
		SetViewResult(quizDto.ViewResult).
		Save(context.Background())
	if err != nil {
		return nil, errors.Wrapf(xerr.NewErrCodeAndInformation(xerr.ServerCommonError), "err:%+v", err)
	}
	return &row.ID, nil
}
