package com.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.util.InfoUtils.Info;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {
    public static void responseMessage(HttpServletResponse response, Info info) {
        responseMessage(response, info, 1);
    }

    public static void responseMessage(HttpServletResponse response, Info info, int flag) {
        final String flag1 = "yyyy-MM-dd";
        final String flag2 = "yyyy-MM-dd HH:mm";
        String endFlag =null;

        switch (flag){
            case 1:
                endFlag=flag1;
                break;
            case 2:
                endFlag=flag2;
                break;
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Token, Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Expose-Headers", "Token");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = JSON.toJSONStringWithDateFormat(info, endFlag);

        assert out != null;
        out.write(json);
        out.flush();
        out.close();
    }
}
