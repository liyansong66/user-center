package com.lys.usercenter.model.domain.Request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 * @Author lys
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户账户、密码、校验密码
    private String userAccount;

    private String userPassword;


}