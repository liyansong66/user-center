package com.lys.usercenter.contant;

/**
 * @description 用户中心常量
 * @Author lys
 */
public interface UserContent {
    /**
     * 用户登录态
     */
    String USER_LOGIN_STATE = "userLoginState";
    //默认权限
    int DEFAULT_ROLE = 0;
    //管理员权限
    int ADMIN_ROLE = 1;
    //在线状态
    /**
     * 在线状态 1 : 在线 2 离开 3 忙碌 0 离线
     */
    Integer ONLINE_STATE = 0;

}
