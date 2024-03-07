package com.lys.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态  0 - 正常 1 - 禁用
     */
    private Integer userStatus;
    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;
    /**
     * 创建时间（数据插入时间）
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除 0 1（逻辑删除）
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 最后一次操作时间
     */
    private Date last_time;

    /**
     * 用户登录状态,1 : 在线 2 离开 3 忙碌 0 离线
     */
    private Integer onlineStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}