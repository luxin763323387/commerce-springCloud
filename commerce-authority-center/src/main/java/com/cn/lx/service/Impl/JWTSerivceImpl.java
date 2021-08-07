package com.cn.lx.service.Impl;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.cn.lx.constant.AuthorityConstant;
import com.cn.lx.constant.CommonConstant;
import com.cn.lx.dao.EcommerceUserDao;
import com.cn.lx.entity.EcommerceUser;
import com.cn.lx.service.IJWTService;
import com.cn.lx.vo.LoginUserInfo;
import com.cn.lx.vo.UserNameAndPassword;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * JWT 相关服务接口实现
 * @author StevenLu
 * @date 2021/8/7 上午11:34
 */
@Slf4j
@Service
public class JWTSerivceImpl implements IJWTService {

    @Autowired
    private EcommerceUserDao userDao;

    @Override
    public String generateToken(String userName, String password) throws Exception {
        return  generateToken(userName,password,0);
    }

    @Override
    public String generateToken(String userName, String password, Integer expire) throws Exception {

        EcommerceUser user = userDao.findByUsernameAndPassword(userName, password);
        if(Objects.isNull(user)){
            log.error("获取token失败-用户不存在:{}:{}",userName,password);
            return null;
        }

        if(Objects.isNull(expire) || expire <= 1){
            expire = AuthorityConstant.DEFAULT_EXPIRE_DAY;
        }

        LoginUserInfo loginUserInfo = new LoginUserInfo(
                user.getId(), user.getUsername()
        );

        ZonedDateTime zonedDateTime = LocalDate.now().plus(expire, ChronoUnit.DAYS)
                .atStartOfDay(ZoneId.systemDefault());
        Date expireDate = Date.from(zonedDateTime.toInstant());

        String token = Jwts.builder()
                // jwt payload --> KV
                .claim(CommonConstant.JWT_USE_INFO_KEY, JSON.toJSONString(loginUserInfo))
                // jwt id
                .setId(UUID.randomUUID().toString())
                // jwt 过期时间
                .setExpiration(expireDate)
                // jwt 签名 --> 加密
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();

        return token;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String registerUserAndGenerateToken(UserNameAndPassword userNameAndPassword) throws Exception {
        EcommerceUser user = userDao.findByUsername(userNameAndPassword.getUserName());

        if(Objects.nonNull(user)){
            log.error("用户注册失败-当前用户名以存在:{}",userNameAndPassword.getUserName());
            return null;
        }

        EcommerceUser ecommerceUser = new EcommerceUser();
        ecommerceUser.setUsername(userNameAndPassword.getUserName());
        ecommerceUser.setPassword(userNameAndPassword.getPassword());
        ecommerceUser.setExtraInfo("{}");
        userDao.save(ecommerceUser);
        log.info("用户注册账号-注册成功:{}，密码:{}",userNameAndPassword.getUserName(),userNameAndPassword.getPassword());


        return generateToken(userNameAndPassword.getUserName(),userNameAndPassword.getPassword());
    }

    /**
     * <h2>根据本地存储的私钥获取到 PrivateKey 对象</h2>
     * */
    private PrivateKey getPrivateKey() throws Exception {

        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(AuthorityConstant.PRIVATE_KEY));
        KeyFactory  keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPKCS8);
    }
}
