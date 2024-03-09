package com.lys.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lys.usercenter.exception.BusinessException;
import com.lys.usercenter.model.domain.Request.UserLoginRequest;
import com.lys.usercenter.model.domain.Request.UserRegisterRequest;
import com.lys.usercenter.model.domain.User;
import com.lys.usercenter.model.domain.common.BaseResponse;
import com.lys.usercenter.model.domain.common.ErrorCode;
import com.lys.usercenter.model.domain.common.ResultUtils;
import com.lys.usercenter.service.userService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lys.usercenter.contant.UserContent.ADMIN_ROLE;
import static com.lys.usercenter.contant.UserContent.USER_LOGIN_STATE;
import static com.lys.usercenter.model.domain.common.ErrorCode.PARAM_ERROR;

/**
 * @description 针对表【user(用户)】的数据库操作Controller
 * @Author lys
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://43.136.41.122/"},methods = {RequestMethod.POST},allowCredentials = "true",allowedHeaders = "*")
public class UserController {

    @Resource
    private userService userService;
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        }
        long result= userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {


        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        User result= userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }
//    //用户获取接口
//    @GetMapping("/current")
//    public User getCurrentUser(HttpServletRequest request) {
//        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
//        User currentUser = (User) userObj;
//            if (currentUser == null) {
//            return null;
//        }
//            // 获取当前用户的id
//            long userId = currentUser.getId();
//            // TODO: 校验用户id的合法性
//            User user = userService.getById(userId);
//        return userService.getSafetyUser(user);
//    }
    //用户获取接口
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentStatusUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            // 用户未登录，返回状态码40100
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        // 获取当前用户的id
        long userId = currentUser.getId();
        // TODO: 校验用户id的合法性
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResponseEntity.ok(safetyUser);
    }

    //用户登出接口
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if(request==null){
            throw new BusinessException(ErrorCode.NULL_REQUEST);
        }

        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    //用户查询接口
    @GetMapping("/search")
    public List<User> searchUsers(String username , HttpServletRequest request) {

        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList= userService.list(queryWrapper);
        List<User> userList1= userList.stream().map(originUser -> userService.getSafetyUser(originUser)).collect(Collectors.toList());
            return userList1;
    }
    //用户删除接口
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
       if(!isAdmin(request)){
           throw new BusinessException(ErrorCode.NO_AUTH);
       }
         if (id <= 0) {
             throw new BusinessException(PARAM_ERROR);
         }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
     }
        //用户更新接口
        @PostMapping("/update")
        public BaseResponse<Boolean> updateUser(@RequestBody User user, HttpServletRequest request) {
            if(!isAdmin(request)){
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            if (user == null) {
                throw new BusinessException(ErrorCode.PARAM_ERROR);
            }
            boolean b = userService.updateById(user);
            return ResultUtils.success(b);
        }
     private boolean isAdmin(HttpServletRequest request) {//仅管理员可查询
         Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
         User user = (User) userObj;
         // 检查用户的角色是否为管理员
         return user != null && user.getUserRole() == ADMIN_ROLE;
     }



    @PostMapping("/status")
    public BaseResponse<Integer> updateUserStatus(HttpServletRequest request,@RequestBody Map<String, Object> requestBody) {
        // 从请求体中获取参数
        String userAccount = (String) requestBody.get("userAccount");
        Integer onlineStatus = (Integer) requestBody.get("onlineStatus");

        // 调用服务层方法进行处理
        userService.updateUserStatus(request, userAccount, onlineStatus);
        return ResultUtils.success(1);
    }
}
