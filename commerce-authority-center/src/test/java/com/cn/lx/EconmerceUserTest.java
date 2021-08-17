package com.cn.lx;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.cn.lx.dao.EcommerceUserDao;
import com.cn.lx.entity.EcommerceUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author StevenLu
 * @date 2021/8/5 下午10:42
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EconmerceUserTest {

    @Autowired
    private EcommerceUserDao ecommerceUserDao;

    @Test
    public void add() {
        EcommerceUser ecommerceUser = new EcommerceUser();
        ecommerceUser.setUsername("luxin");
        ecommerceUser.setPassword(MD5.create().digestHex("123456"));
        ecommerceUser.setExtraInfo("{}");
        ecommerceUserDao.save(ecommerceUser);
    }

    @Test
    public void find() {

        EcommerceUser luxin = ecommerceUserDao.findByUsername("luxin");
        System.out.println(JSON.toJSONString(luxin));

        EcommerceUser luxin1 = ecommerceUserDao.findByUsernameAndPassword("luxin", MD5.create().digestHex("123456"));
        System.out.println(JSON.toJSONString(luxin1));
    }


}
