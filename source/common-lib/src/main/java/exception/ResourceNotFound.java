package exception;

import dto.ErrorCode;

public class ResourceNotFound extends LmsDomainException {

    public ResourceNotFound() {
        super(ErrorCode.RESOURCE_NOTFOUND.getCode(), "Resource not found");
    }
}
