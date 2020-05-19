package com.filter;

import com.alibaba.fastjson.JSON;
import com.service.impl.UserService;
import com.util.InfoUtils.Info;
import com.util.InfoUtils.InfoUtil;
import com.util.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 判断用户是否验证过邀请码的拦截器
 */
public class InviteCodeInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = MyUtils.getUserId();
        if (userService.getUserVerified(userId) == 1) {
            return true;
        } else {
            Info info = InfoUtil.getInfo(605, "请输入邀请码后再来访问");
            responseMessage(response, info);
        }
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
