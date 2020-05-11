package com.util.InfoUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * web层统一返回类型
 */
public class Info {
    @JSONField(ordinal = 1)
    private int code = 0;

    @JSONField(ordinal = 2)
    private String message;

    @JSONField(ordinal = 3)
    private Object data;

    public Info() {

    }


    public Info(Object data) {
        this.data = data;
    }

    public Info(InfoEnum infoEnum) {
        this.code = infoEnum.getCode();
        this.message = infoEnum.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Info{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
