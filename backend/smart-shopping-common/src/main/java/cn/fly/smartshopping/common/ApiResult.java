package cn.fly.smartshopping.common;

public record ApiResult<T>(
        int code,
        String message,
        T data
) {
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "success", data);
    }

    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(500, message, null);
    }
}
