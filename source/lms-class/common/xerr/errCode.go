package xerr

const OK uint32 = 0

const ServerCommonError uint32 = 100001
const RequestParamError uint32 = 100002
const TokenExpireError uint32 = 100003
const TokenGenerateError uint32 = 100004
const DbError uint32 = 100005
const DbUpdateAffectedZeroError uint32 = 100006
const ResourceNotFound uint32 = 100007
