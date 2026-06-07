package com.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.railway.config.UserPrincipal;
import com.railway.dto.LoginDTO;
import com.railway.dto.RegisterDTO;
import com.railway.entity.User;
import com.railway.mapper.UserMapper;
import com.railway.service.UserService;
import com.railway.utils.JwtUtils;
import com.railway.vo.LoginVO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 查询用户
        User user = this.lambdaQuery()
                .eq(User::getPhone, loginDTO.getPhone())
                .one();

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 更新登录时间和IP
        user.setLastLoginTime(LocalDateTime.now());
        this.updateById(user);

        // 生成Token
        String token = jwtUtils.generateToken(user.getId(), user.getPhone());

        // 构建响应
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(user.getId());
        loginVO.setPhone(user.getPhone());
        loginVO.setNickname(user.getNickname());
        loginVO.setAvatar(user.getAvatar());
        loginVO.setIsVerified(user.getIsVerified() == 1);
        loginVO.setToken(token);

        return loginVO;
    }

    @Override
    @Transactional
    public LoginVO register(RegisterDTO registerDTO) {
        // 检查手机号是否已注册
        User existUser = this.lambdaQuery()
                .eq(User::getPhone, registerDTO.getPhone())
                .one();

        if (existUser != null) {
            throw new RuntimeException("手机号已注册");
        }

        // 创建用户
        User user = new User();
        user.setPhone(registerDTO.getPhone());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : "用户" + registerDTO.getPhone().substring(7));
        user.setIsVerified(0);
        user.setStatus(1);
        this.save(user);

        // 生成Token
        String token = jwtUtils.generateToken(user.getId(), user.getPhone());

        // 构建响应
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(user.getId());
        loginVO.setPhone(user.getPhone());
        loginVO.setNickname(user.getNickname());
        loginVO.setIsVerified(false);
        loginVO.setToken(token);

        return loginVO;
    }

    @Override
    public void sendVerifyCode(String phone) {
        // 模拟发送验证码（实际项目中应对接短信服务）
        // 这里简单处理，验证码为123456
        System.out.println("发送验证码到手机：" + phone + "，验证码：123456");
    }

    @Override
    public User getUserInfo(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 清除敏感信息
        user.setPassword(null);
        return user;
    }

    @Override
    @Transactional
    public void updateUserInfo(Long userId, User user) {
        User existUser = this.getById(userId);
        if (existUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 只更新允许修改的字段
        existUser.setNickname(user.getNickname());
        existUser.setAvatar(user.getAvatar());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        this.updateById(existUser);
    }

    @Override
    @Transactional
    public void verifyRealName(Long userId, String realName, String idCard) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查身份证号是否已被使用
        User existUser = this.lambdaQuery()
                .eq(User::getIdCard, idCard)
                .ne(User::getId, userId)
                .one();

        if (existUser != null) {
            throw new RuntimeException("该身份证号已被其他账号使用");
        }

        // 模拟实名认证（实际项目中应对接公安系统API）
        // 这里简单处理，直接标记为已认证
        user.setRealName(realName);
        user.setIdCard(idCard);
        user.setIsVerified(1);
        user.setVerifyTime(LocalDateTime.now());

        this.updateById(user);
    }

    /**
     * 获取当前登录用户ID
     */
    public static Long getCurrentUserId() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUserId();
    }
}