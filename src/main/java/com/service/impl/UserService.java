package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.constant.consist.BaseValue;
import com.constant.enums.Campus;
import com.constant.enums.Position;
import com.model.entity.User;
import com.mapper.UserMapper;
import com.service.intf.IUserService;
import com.util.InfoUtils.Info;
import com.util.InfoUtils.InfoEnum;
import com.util.InfoUtils.InfoUtil;
import com.util.JwtUtil;
import com.util.MyUtils;
import com.util.ParseUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Value("${invite_code}")
    String inviteCode;

    @Override  //登录
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Info login(String userId, String password) {
        //首先登录，若返回token不为空，则登录成功
        String token = getTokenByLogin(userId, password);
        if (token.equals("")) {
            //登录失败
            return InfoUtil.getInfo(1, "对不起，你的你的学号或密码输错了");
        }

        //判断用户是否存在
        Info userInfo = getUserInfo(userId);
        //用户不存在
        if (userInfo.getCode() != 0) {
            //通过token和i山大接口获取用户信息
            User user = getUserInfoByToken(token);
            //插入新的用户
            if (!insertUser(user)) {
                return InfoUtil.getInfo(InfoEnum.FAIL);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", JwtUtil.sign(userId));
                jsonObject.put("verified", 0);
                return InfoUtil.getInfo(jsonObject);
            }
        }

        //用户存在
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", JwtUtil.sign(userId));
        //获取用户verified
        jsonObject.put("verified", getUserVerified(userId));
        return InfoUtil.getInfo(jsonObject);
    }


    @Override  //判断邀请码是否正确
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Info judgeInviteCode(String inviteCode) {
        //邀请码正确
        if (this.inviteCode.equals(inviteCode)) {
            setUserVerified(1);//设置verified为验证过
            return InfoUtil.getInfo(InfoEnum.SUCCESS);
        } else {
            return InfoUtil.getInfo(1, "邀请码错误");
        }
    }

    @Override  //无参获取用户信息,默认为当前用户
    public Info getUserInfo() {
        String userId = MyUtils.getUserId();
        return getUserInfo(userId);
    }

    //通过userId学号获取用户信息||判断用户是否存在
    public Info getUserInfo(String userId) {
        User user = userMapper.getUserInfo(userId);
        //请求失败或没有此用户
        if (user == null) {
            return InfoUtil.getInfo(InfoEnum.FAIL);
        }
        //将数据库中储存的数字转化为枚举类中的详细信息
        if (user.getPosition() != null) {
            String position = Position.getNameByCode(user.getPosition());
            user.setPosition(position);
        }
        if (user.getCampus() != null) {
            String d = user.getCampus();
            String campus = Campus.getNameByCode(user.getCampus());
            user.setCampus(campus);
        }
        return InfoUtil.getInfo(user);
    }

    @Override  //插入新的用户
    public boolean insertUser(User user) {
        return userMapper.insertUser(user) > 0;
    }

    @Override  //更新用户信息
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Info updateUserInfo(User user) {
        user.setUserId(MyUtils.getUserId());

        //将枚举类中的详细信息转化为数据库中储存的数字
        if (user.getPosition() != null) {
            String position = Position.getCodeByName(user.getPosition());
            if (position.equals("-1")) {
                return InfoUtil.getInfo(1, "不存在此职位");
            }
            user.setPosition(position);
        }
        if (user.getCampus() != null) {
            String campus = Campus.getCodeByName(user.getCampus());
            if (campus.equals("-1")) {
                return InfoUtil.getInfo(2, "不存在此校区");
            }
            user.setCampus(campus);
        }
        if (user.getFlag() != null && user.getFlag() != 1 && user.getFlag() != 0) {
            return InfoUtil.getInfo(3, "flag传入值只能为0或1");
        }

        System.out.println(user);
        int result = userMapper.updateUserInfo(user);
        if (result > 0) {
            return InfoUtil.getInfo(InfoEnum.SUCCESS);
        } else {
            return InfoUtil.getInfo(InfoEnum.FAIL);
        }
    }

    @Override  //获取用户verified
    public Integer getUserVerified(String userId) {
        return userMapper.getUserVerified(userId);
    }


    @Override  //设置用户verified
    public boolean setUserVerified(Integer verified) {
        return userMapper.setUserVerified(verified, MyUtils.getUserId()) > 0;
    }

    //登录i山大接口获取token
    private String getTokenByLogin(String userId, String password) {
        String token = "";
        int time = 0;
        while (time++ < 3) { //设置请求次数最多为3次
            try {
                //请求登录接口
                Connection.Response response = Jsoup.connect(BaseValue.loginURL)
                        .method(Connection.Method.POST)
                        .data("u", userId, "p", password)
                        .ignoreContentType(true)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:70.0) Gecko/20100101 Firefox/70.0")
                        .execute();
                if (response.statusCode() == 200) {
                    //获取到的json字符串
                    String ans = response.body();
                    //判断用户名密码是否正确
                    if (ParseUtil.getFieldFromJson(ans, "code").equals("0")) {
                        //从其中解析
                        List<String> data = ParseUtil.getStringArray(ParseUtil.getFieldFromJson(ans, "data"));
                        token = data.get(0);
                    } else {
                        break;//密码错误直接退出
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //如果token不为空，则请求成功
            if (!token.equals("")) {
                break;
            }
        }
        return token;
    }

    //利用token获取用户姓名，学院
    private User getUserInfoByToken(String token) {
        User user = null;
        int time = 0;
        while (time++ < 3) { //设置请求次数最多为3次
            try {
                //请求获取用户信息的接口
                Connection.Response response = Jsoup.connect(BaseValue.userInfoURL)
                        .method(Connection.Method.GET)
                        .header("Token", token)
                        .ignoreContentType(true)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:70.0) Gecko/20100101 Firefox/70.0")
                        .execute();
                if (response.statusCode() == 200) {
                    //获取到的json字符串
                    String ans = response.body();
                    //若请求成功
                    if (ParseUtil.getFieldFromJson(ans, "code").equals("0")) {
                        //从其中解析
                        String data = ParseUtil.getFieldFromJson(ans, "data");
                        String userId = ParseUtil.getFieldFromJson(data, "casId");
                        String name = ParseUtil.getFieldFromJson(data, "name");
                        String academy = ParseUtil.getFieldFromJson(data, "depart");
                        user = new User();
                        user.setUserId(userId);
                        user.setName(name);
                        user.setAcademy(academy);
                    } else {
                        break; //code不为0直接退出
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //如果user不为空且姓名、学院不为空，则请求成功
            if (user != null && MyUtils.NOTNULL(user.getName(), user.getAcademy())) {
                break;
            }
        }
        return user;
    }

}
