package com.service.intf;

import com.model.entity.User;
import com.util.InfoUtils.Info;

public interface IUserService {
    //登录
    Info login(String userId, String password);

    //获取用户信息
    Info getUserInfo();

    //验证邀请码是否正确
    Info judgeInviteCode(String inviteCode);

    //更新用户信息，可更新的属性为campus、department、position、flag，可为空
    Info updateUserInfo(User user);


    //插入新的用户
    boolean insertUser(User user);

    //判断用户是否验证过
    Integer getUserVerified(String userId);

    //设置用户是否验证过
    boolean setUserVerified(Integer verified);
}
