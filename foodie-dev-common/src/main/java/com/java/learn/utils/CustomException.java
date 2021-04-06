package com.java.learn.utils;

import com.java.learn.enums.ExceptionCodeEnum;

public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ExceptionCodeEnum exceptionCode;

    public CustomException(ExceptionCodeEnum exceptionCode) {
        super(exceptionCode.msg);
        this.exceptionCode = exceptionCode;
    }

    public CustomException(ExceptionCodeEnum exceptionCode, String message) {
        super(message != null ? message : exceptionCode.msg);
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCodeEnum getExceptionCode() {
        return exceptionCode;
    }
}
