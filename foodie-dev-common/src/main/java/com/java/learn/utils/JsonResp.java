package com.java.learn.utils;

public class JsonResp {
    /**
     *
     * @Title: AjaxResp.java
     * @Package com.java.learn.utils
     * @Description: json返回对象封装
     */
    private int status;

    private String msg;

    private Object data;

    public JsonResp(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static JsonResp success(Object data) {
        return new JsonResp(200, "success", data);
    }

    public static JsonResp success(int status) {
        return new JsonResp(status, "success", null);
    }

    public static JsonResp success(int status, String msg) {
        return new JsonResp(status, msg, null);
    }

    public static JsonResp success() {
        return new JsonResp(200, "success", null);
    }


    public static JsonResp error(String msg) {
        return new JsonResp(500, msg, null);
    }

    public static JsonResp error(Object data) {
        return new JsonResp(500, "error", data);
    }

    public static JsonResp error(int status, String msg) {
        return new JsonResp(status, msg, null);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
