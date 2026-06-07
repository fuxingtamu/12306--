package com.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.railway.dto.LoginDTO;
import com.railway.dto.RegisterDTO;
import com.railway.entity.User;
import com.railway.vo.LoginVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户注册
     */
    LoginVO register(RegisterDTO registerDTO);

    /**
     * 发送验证码
     */
    void sendVerifyCode(String phone);

    /**
     * 获取用户信息
     */
    User getUserInfo(Long userId);

    /**
     * 更新用户信息
     */
    void updateUserInfo(Long userId, User user);

    /**
     * 实名认证
     */
    void verifyRealName(Long userId, String realName, String idCard);
}