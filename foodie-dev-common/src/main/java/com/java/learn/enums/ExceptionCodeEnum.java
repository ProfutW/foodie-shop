package com.java.learn.enums;

public enum ExceptionCodeEnum {
    NO_STOCK(422, "库存不足");

    public final Integer code;
    public final String msg;

    ExceptionCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
