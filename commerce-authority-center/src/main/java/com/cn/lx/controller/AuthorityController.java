package com.cn.lx.controller;

import com.alibaba.fastjson.JSON;
import com.cn.lx.annotation.IgnoreResponseAdvice;
import com.cn.lx.service.IJWTService;
import com.cn.lx.vo.JwtToken;
import com.cn.lx.vo.UserNameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author StevenLu
 * @date 2021/8/7 下午3:48
 */
@Slf4j
@RestController
@RequestMapping("/authority")
public class AuthorityController {


    @Autowired
    private IJWTService ijwtService;

    @PostMapping("/token")
    @IgnoreResponseAdvice
    public JwtToken token(@RequestBody UserNameAndPassword userNameAndPassword) throws Exception {

        log.info("用户获取token参数:{}", JSON.toJSONString(userNameAndPassword));
        String token = ijwtService.generateToken(userNameAndPassword.getUserName(), userNameAndPassword.getPassword());
        return new JwtToken(token);
    }


    @PostMapping("/register")
    @IgnoreResponseAdvice
    public JwtToken register(@RequestBody UserNameAndPassword userNameAndPassword) throws Exception {

        log.info("用户注册:{}", JSON.toJSONString(userNameAndPassword));
        String string = ijwtService.registerUserAndGenerateToken(userNameAndPassword);
        JwtToken token = new JwtToken(string);
        return token;
    }

}
