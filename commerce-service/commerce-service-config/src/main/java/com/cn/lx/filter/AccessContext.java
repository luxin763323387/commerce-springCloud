package com.cn.lx.filter;

import com.cn.lx.vo.LoginUserInfo;

/**
 * 使用 ThreadLocal 去单独存储每一个线程携带的 LoginUserInfo 信息
 * 要及时的清理我们保存到 ThreadLocal 中的用户信息:
 * 1. 保证没有资源泄露
 * 2. 保证线程在重用时, 不会出现数据混乱
 *
 * @author StevenLu
 * @date 2021/8/17 下午10:23
 */
public class AccessContext {

    private static final ThreadLocal<LoginUserInfo> userInfoThreadLocal = new ThreadLocal<>();

    public static LoginUserInfo getLoginUserInfo() {
        return userInfoThreadLocal.get();
    }


    public static void setLoginUserInfo(LoginUserInfo loginUserInfo) {
        userInfoThreadLocal.set(loginUserInfo);
    }

    public static void remove() {
        userInfoThreadLocal.remove();
    }

}
