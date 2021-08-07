package com.cn.lx.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author StevenLu
 * @date 2021/8/7 上午10:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNameAndPassword {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

}
