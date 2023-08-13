package xerr

var message map[uint32]string

func init() {
	message = make(map[uint32]string)
	message[OK] = "SUCCESS"
	message[ServerCommonError] = "there's been problem in server, please try again later"
	message[RequestParamError] = "request param is not correct"
	message[DbError] = "the database is not working now, please try again later"
	message[ResourceNotFound] = "requested %s is not found"
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
