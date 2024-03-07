package com.lys.usercenter.model.domain.common;

import com.lys.usercenter.model.domain.User;
import lombok.Data;

import javax.xml.ws.Response;
import java.io.Serializable;
import java.util.List;

/**
 * @description 通用返回类
 * @Author lys
 */
@Data
public class BaseResponse <T>implements Serializable {
    private int code;
    private String message;
    private T data;
    private String description;

    public BaseResponse(int code,T data,String message){
        this(code,message,data,"");
    }

    public BaseResponse(int code, T data) {
        this(code,data,"");
    }

    public BaseResponse(int code, String message, T data, String description) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null,errorCode.getDescription());

    }


}
