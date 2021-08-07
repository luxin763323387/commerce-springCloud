package com.cn.lx.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author StevenLu
 * @date 2021/8/7 上午10:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserInfo {

    /**
     * 登入id
     */
    private Long uid;

    /**
     * 用户名  唯一索引
     */
    private String userName;
}
