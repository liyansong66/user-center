package com.lys.usercenter.model.domain.common;

/**
 * @description 错误码
 * @Author lys
 */
public enum ErrorCode {

    SUCCESS(0, "成功", ""),
    PARAM_ERROR(40000, "参数错误", ""),
    NULL_REQUEST(40006, "请求为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    USER_NOT_FOUND(40001, "用户不存在", ""),
    USER_ACCOUNT_ERROR(40002, "账号错误", ""),
    USER_PASSWORD_SHORT(40003, "密码过短", ""),
    USER_PASSWORD_ERROR(40008, "密码错误", ""),
    USER_PASSWORD_INVALID(40004, "密码无效", ""),
    USER_ACCOUNT_EXIST(40005, "用户已存在", ""),
    USER_REGISTER_ERROR(40006, "用户注册失败", ""),
    USER_LOGIN_ERROR(40007, "用户登录失败", ""),
    SYSTEM_ERROR(50000, "系统内部异常","" ),
    USER_ACCOUNT_INVALID(40010,"用户名非法" ,"" );


    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }
}
