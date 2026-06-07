package com.railway.controller;

import com.railway.common.Result;
import com.railway.config.UserPrincipal;
import com.railway.dto.LoginDTO;
import com.railway.dto.RegisterDTO;
import com.railway.entity.User;
import com.railway.service.UserService;
import com.railway.service.impl.UserServiceImpl;
import com.railway.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证接口", description = "用户登录、注册、验证码等接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            LoginVO loginVO = userService.login(loginDTO);
            return Result.success("登录成功", loginVO);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<LoginVO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        try {
            LoginVO loginVO = userService.register(registerDTO);
            return Result.success("注册成功", loginVO);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 发送验证码
     */
    @Operation(summary = "发送验证码")
    @PostMapping("/send-code")
    public Result<Void> sendVerifyCode(@RequestParam String phone) {
        try {
            userService.sendVerifyCode(phone);
            return Result.success("验证码已发送", null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/user-info")
    public Result<User> getUserInfo() {
        try {
            Long userId = getCurrentUserId();
            User user = userService.getUserInfo(userId);
            return Result.success(user);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户信息")
    @PutMapping("/user-info")
    public Result<Void> updateUserInfo(@RequestBody User user) {
        try {
            Long userId = getCurrentUserId();
            userService.updateUserInfo(userId, user);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 实名认证
     */
    @Operation(summary = "实名认证")
    @PostMapping("/verify")
    public Result<Void> verifyRealName(@RequestParam String realName, @RequestParam String idCard) {
        try {
            Long userId = getCurrentUserId();
            userService.verifyRealName(userId, realName, idCard);
            return Result.success("实名认证成功", null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUserId();
    }
}