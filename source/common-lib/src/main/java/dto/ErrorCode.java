package dto;

import lombok.Getter;

@Getter
public enum ErrorCode {
    RESOURCE_NOTFOUND(404L);

    private final Long code;

    private ErrorCode(Long code) {
        this.code = code;
    }
}
