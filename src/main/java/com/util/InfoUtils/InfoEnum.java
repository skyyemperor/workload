package com.util.InfoUtils;

public enum InfoEnum {
    /**
     * API调用成功返回
     */
    SUCCESS(0, "success!!"),
    FAIL(-1, "服务器错误"),
    AUTH_ERROR(601, "认证失败，请重新登录"),
    PARAMS_NULL(602, "请求字段不能为空"),
    ;

    private int code;

    private String message;


    private InfoEnum(int code, String message) {
        this.code = code;
        this.message = message;
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

}
