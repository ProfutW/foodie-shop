package com.java.learn.enums;

public enum PayType {
    WECHAT(1, "微信"),
    ALIPAY(2, "支付宝");

    public final Integer type;
    public final String value;

    PayType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
