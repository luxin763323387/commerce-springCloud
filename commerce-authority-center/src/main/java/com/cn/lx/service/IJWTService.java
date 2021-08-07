package com.cn.lx.service;

import com.cn.lx.vo.UserNameAndPassword;

/**
 * JWT 相关服务接口
 * @author StevenLu
 * @date 2021/8/7 上午11:27
 */
public interface IJWTService {

    /**
     * 获取 token 有效期默认1天
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    String generateToken(String userName,String password) throws Exception;

    /**
     * 获取token
     * @param userName
     * @param password
     * @param expire
     * @return
     * @throws Exception
     */
    String generateToken(String userName,String password,Integer expire) throws Exception;


    /**
     * 注册并且会获取 token
     * @param userNameAndPassword
     * @return
     */
    String registerUserAndGenerateToken(UserNameAndPassword userNameAndPassword) throws Exception;
}
