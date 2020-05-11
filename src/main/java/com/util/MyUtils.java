package com.util;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyUtils {

    public static Integer convertString2Integer(String strVal) {
        if (strVal == null || strVal.equals("")) {
            return null;
        } else {
            return Integer.valueOf(strVal);
        }
    }


    public static <T> T getNewInstance(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (Exception e) {
            System.out.println("公用方法：实例化对象异常：" + e.getMessage());
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将日期类型转为指定格式的字符串
     *
     * @param date       被转换的日期类型
     * @param strPattern 日期格式串
     * @return 转换之后的字符串
     */
    public static String convertDate2String(Date date, String strPattern) {
        // 如果时间对象为null则，返回当前时间
        if (date == null) {
            date = new Date();
        }
        // 如果格式串为空则使用默认格式
        if (strPattern == null || strPattern.equals("")) {
            // 默认格式串
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        // 创建一个日期对象转换类
        SimpleDateFormat sdf = new SimpleDateFormat(strPattern);
        // 返回转换之后的字符串
        return sdf.format(date);
    }


    /**
     * 将指定格式的字符串转换为日期类型 (strPattern的格式表示是strDate的格式)
     *
     * @param strDate    日期字符串
     * @param strPattern 日期字符串的格式
     * @return 转换之后的日期对象
     */
    public static Date convertString2Date(String strDate, String strPattern) {
        // 如果时间对象为null则，返回当前时间


        if (strDate == null || strDate.equals("")) {
            return new Date();
        }
        // 如果格式串为空则使用默认格式
        if (strPattern == null || strPattern.equals("")) {
            // 默认格式串
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        // 创建一个日期对象转换类
        SimpleDateFormat sdf = new SimpleDateFormat(strPattern);
        // 返回转换之后的时间对象
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            // 公用方法：字符串转换日期对象异常：
            //logRootLogger.error("公用方法-字符串转换日期对象异常：" + e.getMessage());
            e.printStackTrace();
        }
        return date;
    }

    public static Double formatDouble(Double d) {
        //保留小数点后两位数
        return (double) Math.round(d * 100) / 100;
    }

    public static boolean NOTNULL(String str) {
        if (str != null && !"".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean NOTNULL(Object... strs) {
        for (Object str : strs) {
            if (str == null || !NOTNULL(str.toString())) {
                return false;
            }
        }
        return true;
    }

    //获取当前线程的userId
    public static String getUserId() {
        //获取到当前线程绑定的请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //已经拿到session,就可以拿到session中保存的用户信息了。
        return request.getSession().getAttribute("userId").toString();
    }
}
