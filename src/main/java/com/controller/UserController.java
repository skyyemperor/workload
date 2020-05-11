package com.controller;

import com.model.entity.User;
import com.service.impl.UserService;
import com.util.InfoUtils.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workload")
public class UserController {

    @Autowired
    private UserService userService;

    //登录
    @PostMapping("/login")
    public Info login(@RequestParam String userId, @RequestParam String password) {
        return userService.login(userId, password);
    }

    //验证邀请码是否正确
    @PostMapping("/judgeInviteCode")
    public Info judgeInviteCode(@RequestParam String inviteCode) {
        return userService.judgeInviteCode(inviteCode);
    }

    //获取个人信息
    @PostMapping("/getUserInfo")
    public Info getUserInfo() {
        return userService.getUserInfo();
    }

    //修改个人信息
    @PostMapping("/modifyUserInfo")
    public Info modifyUserInfo(
            @RequestParam(required = false) String campus,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) Integer flag) {
        //将参数封装到user对象里
        User user = new User();
        user.setCampus(campus);
        user.setDepartment(department);
        user.setPosition(position);
        user.setFlag(flag);
        return userService.updateUserInfo(user);
    }


}
