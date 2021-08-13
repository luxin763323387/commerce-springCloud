package com.cn.lx.constant;

/**
 * @author StevenLu
 * @date 2021/8/13 下午11:37
 */
public class GatewayConstant {


    /**
     * 登入 uri
     */
    public static final String LOGIN_URI = "commerce/login";


    /**
     * 注册 uri
     */
    public static final String REGISTER_URI = "commerce/register";


    /**
     * 授权中心拿到登陆token 的uri 格式化接口
     */
    public static final String AUTHORITY_CENTER_TOKEN_URI_FORMAT = "http://%s:%s/commerce-authority-center/authority/token";

    /**
     * 授权中心拿到登陆token 的uri 格式化接口
     */
    public static final String AUTHORITY_CENTER_REGISTER_URI_FORMAT = "http://%s:%s/commerce-authority-center/authority/register";


}
