package com.util.InfoUtils;

public class InfoUtil {

    public static Info getInfo(Object data) {
        return getInfo(InfoEnum.SUCCESS.getCode(), InfoEnum.SUCCESS.getMessage(), data);
    }

    public static Info getInfo(InfoEnum infoEnum) {
        return getInfo(infoEnum.getCode(), infoEnum.getMessage());
    }

    public static Info getInfo(int code, String msg) {
        return getInfo(code, msg, null);
    }

    public static Info getInfo(int code, String message, Object data) {
        Info info = new Info(data);
        info.setCode(code);
        info.setMessage(message);
        return info;
    }

}
