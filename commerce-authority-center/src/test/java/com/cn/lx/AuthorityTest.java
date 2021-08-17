package com.cn.lx;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.cn.lx.service.IJWTService;
import com.cn.lx.util.TokenParseUtil;
import com.cn.lx.vo.LoginUserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author StevenLu
 * @date 2021/8/7 下午5:06
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthorityTest {

    @Autowired
    private IJWTService ijwtService;

    @Test
    public void parseTokenTest() throws Exception {

        System.out.println(MD5.create().digestHex("123456"));
        String token = ijwtService.generateToken("luxin", "e10adc3949ba59abbe56e057f20f883e");

        LoginUserInfo loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        System.out.println(JSON.toJSONString(loginUserInfo));
    }


}
