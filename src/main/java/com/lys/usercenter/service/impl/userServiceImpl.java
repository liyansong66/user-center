package com.lys.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lys.usercenter.exception.BusinessException;
import com.lys.usercenter.model.domain.User;
import com.lys.usercenter.model.domain.common.ErrorCode;
import com.lys.usercenter.service.userService;
import com.lys.usercenter.mapper.userMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lys.usercenter.contant.UserContent.USER_LOGIN_STATE;
import static com.lys.usercenter.contant.UserContent.ONLINE_STATE;

/**
* @author lys
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-01-31 19:02:19
*/
@Service
@Slf4j
public  class userServiceImpl extends ServiceImpl<userMapper, User>
    implements userService {

    @Resource
    private userMapper userMapper;
    /**
     * 盐值，混淆密码
     */
    private static final String salt = "123";
    private Long userId;
    private Integer onlineStatus;

    /**
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //非空、用户名密码校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        if (userAccount.length() < 4 || userAccount.length() > 16) {
            throw new BusinessException(ErrorCode.USER_ACCOUNT_ERROR, "用户名过长或过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_SHORT, "密码过短");
        }

        //用户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.USER_ACCOUNT_INVALID, "用户名不能包含特殊字符");
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR, "密码和校验密码不相同");
        }
        //用户账号是否存在
        if (isUserAccountExist(userAccount)) {
            //用户账号已存在
            throw new BusinessException(ErrorCode.USER_ACCOUNT_EXIST,"用户账号已存在");
        }
        //对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((userPassword + salt).getBytes());
        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setOnlineStatus(0);
        //判断是否插入成功
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.USER_REGISTER_ERROR, "注册失败,请重试");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //非空、用户名密码校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        if (userAccount.length() < 4 || userAccount.length() > 16) {
            throw new BusinessException(ErrorCode.USER_ACCOUNT_ERROR, "Invalid userAccount length");
        }

        //用户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_INVALID, "Invalid userPassword");
        }
        //对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((userPassword + salt).getBytes());

        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //判断是否查询到用户
        if (user == null) {
            log.info("userAccount or userPassword is error!");
            throw new BusinessException(ErrorCode.USER_LOGIN_ERROR);
        }
        // After the user is authenticated
        // Update the user's status to online and last_time to the current time

            //查询数据库中onlineStatus字段，默认为0，登陆后为1
            if (onlineStatus == ONLINE_STATE) {
                user.setOnlineStatus(1);
            }
            onlineStatus = user.getOnlineStatus();
            // Update the onlineStatus field
            user.setOnlineStatus(1);
            userMapper.updateById(user);
            // Update last_time fields
            user.setLast_time(new Timestamp(System.currentTimeMillis()));
            // Save the changes to the database
            userMapper.updateById(user);


        //用户脱敏
        User safetyUser = getSafetyUser(user);
        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser 用户
     * @return 脱敏后的用户
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setIsDelete(originUser.getIsDelete());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setOnlineStatus(originUser.getOnlineStatus());
        safetyUser.setLast_time(originUser.getLast_time());
        return safetyUser;
    }

    /**
     * 用户登出
     *
     * @param request 请求
     * @return 用户信息
     */
//    @Override
//    public Integer userLogout(HttpServletRequest request) {
//        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
//        log.info("userLogout: " + user);
//        updateUserStatus(user.getId(), 0);
//        user.setLast_time(new Timestamp(System.currentTimeMillis()));
//        userMapper.updateById(user);
//        request.getSession().removeAttribute(USER_LOGIN_STATE);
//        return 1;
//    }
    @Override
    public Integer userLogout(HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (sessionUser != null) {
            // Query the user from the database
            User user = userMapper.selectById(sessionUser.getId());
            if (user != null) {
                userMapper.updateById(user);
            }
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public User getUserLoginState(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(USER_LOGIN_STATE);
    }

    @Override
    public boolean isUserAccountExist(String userAccount) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            //用户账号已存在
            return true;
        }
        return false;
    }

    @Override
    public Integer updateUserStatus(HttpServletRequest request, String userAccount, Integer onlineStatus) {//从前端传来的数据查找用户，并更新用户状态
        User sessionUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (sessionUser != null) {
            // 根据userAccount查询用户
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);//查询条件：用户名
            User user = userMapper.selectOne(queryWrapper);//查询字段为userAccount的用户
            if (user != null) {
                user.setOnlineStatus(0);
                userMapper.update(user, queryWrapper);//更新用户状态
                user.setLast_time(new Timestamp(System.currentTimeMillis()));
                userMapper.update(user, queryWrapper);//更新用户最后登录时间
            }
        }

      return 1;
    }

//    @Override
//    public void updateUserStatusAndTime(Long id, Integer onlineStatus) {
//        User user = userMapper.selectById(id);
//        if (user != null) {
//            //查询数据库中onlineStatus字段，默认为0，登陆后为1
//            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//            if (onlineStatus == ONLINE_STATE) {
//                user.setOnlineStatus(1);
//            }
//            onlineStatus = user.getOnlineStatus();
//            // Update the onlineStatus field
//            queryWrapper.eq("onlineStatus", onlineStatus);
//            // Update last_time fields
//            user.setLast_time(new Timestamp(System.currentTimeMillis()));
//            // Save the changes to the database
//            userMapper.updateById(user);
//        }
//
//    }

}


