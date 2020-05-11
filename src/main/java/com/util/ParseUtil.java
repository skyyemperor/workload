package com.util;

import com.alibaba.fastjson.JSON;
import com.util.InfoUtils.Info;

import java.time.LocalDateTime;
import java.util.List;

public class ParseUtil {

    //将对象转化为json
    public static String toJson(Object object) {
        return toJson(object, 1);
    }

    public static String toJson(Object object, int flag) {
        final String flag1 = "yyyy-MM-dd";
        final String flag2 = "yyyy-MM-dd HH:mm";
        String endFlag = null;

        switch (flag) {
            case 1:
                endFlag = flag1;
                break;
            case 2:
                endFlag = flag2;
                break;
        }
        return JSON.toJSONStringWithDateFormat(object, endFlag);
    }

    //将json转化为对象
    public static <T> T fromJson(String json, Class<T> clazz) {
        T tb = null;
        try {
            tb = JSON.parseObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tb;
    }

    //将json转化为Info对象
    public static Info getInfoFromJson(String json) {
        return fromJson(json, Info.class);
    }

    //获取json的某个字段
    public static String getFieldFromJson(String json,String field){
        return JSON.parseObject(json).getString(field);
    }

    //将json中data对应的dataJson转化为对象
    public static <T> T getDataFromJson(String json, Class<T> clazz) {
        String dataJson = JSON.parseObject(json).getString("data");
        return fromJson(dataJson, clazz);
    }

    //将json转化为Integer数组，例如"[5,7,8]"
    public static List<Integer> getIntArray(String arrayJson) {
        List<Integer> array = JSON.parseArray(arrayJson, Integer.class);
        return array;
    }

    //将json转化为String数组，例如"["hebf","egh2u"]"
    public static List<String> getStringArray(String arrayJson) {
        List<String> array = JSON.parseArray(arrayJson, String.class);
        return array;
    }
}
