package com.railway.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户主体信息
 */
@Data
public class UserPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String phone;

    public UserPrincipal(Long userId, String phone) {
        this.userId = userId;
        this.phone = phone;
    }
}