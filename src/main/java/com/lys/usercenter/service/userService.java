package com.lys.usercenter.service;

import com.lys.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author lys
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-01-31 19:02:19
*/
public interface userService extends IService<User> {




    /**
     * 用户注册
     * @param username 用户名
     * @param userAccount 用户账号
     * @param checkPassword 校验密码
     * @return 用户id
     */
    long userRegister(String username, String userAccount, String checkPassword);
    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取脱敏后的用户信息
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    User getSafetyUser(User originUser);

    /**
     * 用户登出
     * @param request 请求
     * @return 用户信息
     */
    Integer userLogout(HttpServletRequest request);

    User getUserLoginState(HttpServletRequest request);
    /**
     * 判断用户账号是否存在
     * @param userAccount 用户账号
     * @return 是否存在
     */
    boolean isUserAccountExist(String userAccount);


    /**
     * 更新用户状态
     *
     * @param request      请求
     * @param userAccount  用户账号
     * @param onlineStatus 用户状态
     * @return
     */

    Integer updateUserStatus(HttpServletRequest request, String userAccount, Integer onlineStatus);
}
