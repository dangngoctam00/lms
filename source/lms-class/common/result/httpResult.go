package result

import (
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/pkg/errors"
	"github.com/zeromicro/go-zero/core/logx"
	"lms-class/common/xerr"
)

func HttpResult(c *fiber.Ctx, resp interface{}, err error) error {
	if err == nil {
		return c.Status(fiber.StatusOK).JSON(Success(resp))
	} else {
		// default error
		errCode := xerr.ServerCommonError
		errMsg := xerr.MapErrMsg(errCode)

		causeErr := errors.Cause(err)
		if e, ok := causeErr.(*xerr.CodeError); ok {
			errCode = e.GetErrCode()
			errMsg = e.GetErrMsg()
		}

		logx.WithContext(c.Context()).Errorf("【API-ERR】 : %+v ", err)
		return c.Status(fiber.StatusBadRequest).JSON(Error(xerr.RequestParamError, errMsg))
	}
}

func ParamErrorResult(c *fiber.Ctx, err error) error {
	errMsg := fmt.Sprintf("%s, %s", xerr.MapErrMsg(xerr.RequestParamError), err.Error())
	return c.Status(fiber.StatusBadRequest).JSON(Error(xerr.RequestParamError, errMsg))
}
