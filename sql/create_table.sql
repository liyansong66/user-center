-- auto-generated definition
create table user
(
    id           bigint auto_increment comment '用户id'
        primary key,
    username     varchar(256)      null comment '用户昵称',
    userAccount  varchar(256)      null comment '账号',
    avatarUrl    varchar(1024)     null comment '用户头像',
    gender       tinyint           null comment '性别',
    userPassword varchar(512)      null comment '密码',
    phone        varchar(128)      null comment '电话',
    email        varchar(512)      null comment '邮箱',
    userStatus   int     default 0 not null comment '用户状态  0 - 正常 ',
    createTime   timestamp         null comment '创建时间（数据插入时间）',
    updateTime   timestamp         null comment '更新时间',
    isDelete     tinyint default 0 not null comment '是否删除 0 1（逻辑删除）',
    userRole     int     default 0 not null comment '用户角色 0-普通用户 1-管理员',
    last_time    datetime          null comment '最后一次操作时间',
    onlineStatus int     default 0 null comment '用户登录状态,1 : 在线 2 离开 3 忙碌 0 离线'
)
    comment '用户';

CREATE TRIGGER before_insert_user
    BEFORE INSERT
    ON user
    FOR EACH ROW
    SET NEW.createTime = NOW(), NEW.updateTime = NOW();

CREATE TRIGGER before_update_user
    BEFORE UPDATE
    ON user
    FOR EACH ROW
    SET NEW.updateTime = NOW();