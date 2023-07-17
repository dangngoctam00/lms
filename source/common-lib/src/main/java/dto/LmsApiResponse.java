package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LmsApiResponse<T> {

    private Long code;
    private T value;

    public LmsApiResponse(Long code) {
        this.code = code;
    }

    public LmsApiResponse(Long code, T value) {
        this.code = code;
        this.value = value;
    }

    public static <T> LmsApiResponse<T> ok() {
        return new LmsApiResponse<>(200L);
    }

    public static <T> LmsApiResponse<T> ok(T value) {
        return new LmsApiResponse<>(200L, value);
    }
}
