package com.lys.usercenter.model.domain.common;

/**
 * @description 返回工具类
 * @Author lys
 */
public class ResultUtils {
    /**
     * 成功返回
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, "ok", data, "");
    }

    /**
     * 失败返回
     * @param errorCode
     * @return
     */
    public static  BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败返回
     * @param errorCode
     * @param description
     * @return
     */
    public static  BaseResponse error(ErrorCode errorCode,String message, String description) {
        return new BaseResponse<>(errorCode.getCode(),message,null,description);
    }

    public static  BaseResponse error(int code,String message, String description) {
        return new BaseResponse<>(code,message,null,description);
    }
    /**
     * 失败返回
     * @param errorCode
     * @param description
     * @return
     */
    public static  BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(),errorCode.getMessage(),description);
    }

}
