package cn.fly.smartshopping.exception;

import cn.fly.smartshopping.common.Result;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst().map(error -> error.getDefaultMessage()).orElse("请求参数不合法");
        return Result.error(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraint(ConstraintViolationException ex) {
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public Result<Void> handleBadRequest(Exception ex) {
        String message = ex.getMessage() == null ? "请求参数不合法" : ex.getMessage();
        if ("请先登录".equals(message)) return Result.unauthorized(message);
        return Result.error(message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<Void> handleDataIntegrity(DataIntegrityViolationException ex) {
        return Result.error("数据保存失败，请检查数据是否重复或完整");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleOther(Exception ex) {
        return Result.error("服务器内部错误");
    }
}
