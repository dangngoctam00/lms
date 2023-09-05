package pkg

import (
	"context"
	"fmt"
)

var LmsContext context.Context
var SchemaKey = "schema"

func GetTenant() string {
	return fmt.Sprintf("%v", LmsContext.Value(SchemaKey))
}
