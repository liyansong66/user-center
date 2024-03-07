package com.lys.usercenter.service;


import com.lys.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;


/**
 * @description 针对表【user(用户)】的数据库操作Service的单元测试
 * @Author lys
 */
@SpringBootTest
public class userServiceTest {

    @Resource
    private userService userService;
    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("test01");
        user.setUserAccount("123");
        user.setAvatarUrl("https://avatars.githubusercontent.com/u/88729731?s=400&v=4");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("456");
        user.setEmail("xxx");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);

    }
    @Test
    void testUserRegister() {
        String userAccount = "test01";
        String userPassword = "";
        String checkPassword = "123456";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "test02";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount ="l y s";
        userPassword ="12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "lys0101";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);
    }

}