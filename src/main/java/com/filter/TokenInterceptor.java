package com.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.service.impl.UserService;
import com.util.InfoUtils.Info;
import com.util.InfoUtils.InfoEnum;
import com.util.InfoUtils.InfoUtil;
import com.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Token, Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Expose-Headers", "Token");

        String token = request.getHeader("Token");
        //token不存在
        if (null != token) {
            //验证token是否正确
            if (JwtUtil.verify(token)) {
                //获取token中携带的userId
                String userId = JwtUtil.getUserId(token);
                //将userId存到session
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                return true;
            }
        }
        System.out.println("tkjv fd");
        Info info = InfoUtil.getInfo(InfoEnum.AUTH_ERROR);
        responseMessage(response, info);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }


    //向客户端发送消息
    public static void responseMessage(HttpServletResponse response, Info info) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = JSON.toJSONString(info);

        assert out != null;
        out.write(json);
        out.flush();
        out.close();
    }
}