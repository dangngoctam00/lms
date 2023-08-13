package result

type LmsErrorResponseApi struct {
	Code    uint32 `json:"code"`
	Message string `json:"message"`
}

func Error(errCode uint32, errMsg string) *LmsErrorResponseApi {
	return &LmsErrorResponseApi{errCode, errMsg}
}
