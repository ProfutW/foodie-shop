package com.java.learn.enums;

public enum CategoryType {
    FIRST(1, "一级"),
    SECOND(2, "二级"),
    THIRD(3, "三级");

    public final Integer type;
    public final String value;

    CategoryType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
