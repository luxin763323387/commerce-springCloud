package com.cn.lx;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


/**
 * @author StevenLu
 * @date 2021/8/7 上午9:37
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RSATest {

    @Test
    public void KeyPairTest() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        //生成公钥对，私钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();


        String publicEncode = Base64.encode(publicKey.getEncoded());
        String privateEncode = Base64.encode(privateKey.getEncoded());

        log.info("publickey:{}",publicEncode);
        log.info("privatekey:{}",privateEncode);



    }
}
