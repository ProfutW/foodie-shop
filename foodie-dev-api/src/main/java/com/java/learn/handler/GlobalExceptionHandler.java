package com.java.learn.handler;

import com.java.learn.enums.ExceptionCodeEnum;
import com.java.learn.utils.CustomException;
import com.java.learn.utils.JsonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     *  缺少body参数
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JsonResp validateHttpMessageNotReadableException(HttpMessageNotReadableException exception){
        return JsonResp.error(UNPROCESSABLE_ENTITY.value(), exception.getMessage().split(":")[0]);
    }

    /**
     *  body参数验证
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(BindException.class)
    public JsonResp validateBindException(BindException exception){
        List<FieldError> errors = exception.getFieldErrors();
        Map<String, Object> viewErrors = new HashMap<>();

        for (FieldError error : errors) {
            viewErrors.put(error.getField(), error.getDefaultMessage());
        }
        return new JsonResp(UNPROCESSABLE_ENTITY.value(), "参数验证错误", viewErrors);
    }

    /**
     *  缺少param参数
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public JsonResp validateMissingServletRequestParameterException(MissingServletRequestParameterException exception){
        return JsonResp.error(UNPROCESSABLE_ENTITY.value(), exception.getMessage());
    }

    /**
     *  param参数验证
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public JsonResp validateConstraintViolationException(ConstraintViolationException exception){
        Map<String, Object> viewErrors = new HashMap<>();

        for (String error : exception.getMessage().split(",")) {
            String[] errorItem = error.split(":");
            String errorKey = errorItem[0];
            String errorMsg = errorItem[1];
            String[] errorKeys = errorKey.split("\\.");
            String errorLastKey = errorKeys[errorKeys.length - 1];
            viewErrors.put(errorLastKey, errorMsg);
        }
        return new JsonResp(UNPROCESSABLE_ENTITY.value(), "参数验证错误", viewErrors);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JsonResp maxUploadSizeE(MaxUploadSizeExceededException exception) {
        return JsonResp.error(422, "上传文件大于500KB，请选择合适的大小！");
    }

    /**
     * 处理自定义异常
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public JsonResp validateCustom(CustomException exception){

        // 库存不足
        if (exception.getExceptionCode() == ExceptionCodeEnum.NO_STOCK) {
            return JsonResp.error(ExceptionCodeEnum.NO_STOCK.code, exception.getMessage());
        }

        logger.error(exception.getCause().getMessage(), exception);
        return JsonResp.error("内部错误");
    }

    /**
     *  兜底异常捕获
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(RuntimeException.class)
    public JsonResp validateRuntimeException(RuntimeException exception){
        logger.error(exception.getCause().getMessage(), exception);
        return JsonResp.error("内部错误");
    }
}
