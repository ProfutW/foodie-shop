package com.java.learn.enums;

public enum ItemCommendLevel {
    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评");

    public final Integer type;
    public final String value;

    ItemCommendLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
