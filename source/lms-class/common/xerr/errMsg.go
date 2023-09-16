package xerr

var message map[uint32]string

func init() {
	message = make(map[uint32]string)
	message[OK] = "SUCCESS"
	message[ServerCommonError] = "there's been problem in server, please try again later"
	message[RequestParamError] = "request param is not correct"
	message[DbError] = "the database is not working now, please try again later"
	message[ResourceNotFound] = "requested %s is not found"

	message[QuizSessionClosed] = "Quiz session has been closed"
	message[QuizNotConfigured] = "Quiz has not been configured"
	message[QuizSessionIsNotAllowedView] = "Quiz session has not been allowed to view"

	message[QuestionDataNotCorrect] = "Question data is not correct, please check again"
}

func MapErrMsg(errCode uint32) string {
	if msg, ok := message[errCode]; ok {
		return msg
	} else {
		return "unknown error"
	}
}

func IsCodeErr(errCode uint32) bool {
	if _, ok := message[errCode]; ok {
		return true
	} else {
		return false
	}
}
