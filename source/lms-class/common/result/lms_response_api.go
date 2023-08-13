package result

type LmsResponseSuccessApi struct {
	Code    uint32      `json:"code"`
	Message string      `json:"message"`
	Value   interface{} `json:"value,omitempty"`
}

func Success(data interface{}) *LmsResponseSuccessApi {
	return &LmsResponseSuccessApi{200, "OK", data}
}
