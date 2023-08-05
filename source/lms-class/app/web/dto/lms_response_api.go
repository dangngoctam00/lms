package dto

type LmsResponseApi[T any] struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
	Value   T      `json:"value,omitempty"`
}
