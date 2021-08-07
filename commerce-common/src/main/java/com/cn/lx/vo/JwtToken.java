package com.cn.lx.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author StevenLu
 * @date 2021/8/7 上午10:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {


    /**
     * Jwt  字符串信息
     */
    private String token;
}
