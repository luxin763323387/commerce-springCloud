package com.cn.lx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.lx.constant.CommonConstant;
import com.cn.lx.vo.LoginUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Objects;


/**
 * @author StevenLu
 * @date 2021/8/7 下午4:34
 */
public class TokenParseUtil {

    public static LoginUserInfo parseUserInfoFromToken(String token) throws Exception {

        if (Objects.isNull(token)) {
            return null;
        }

        Jws<Claims> claimsJws = parseFromToken(token, getPublicKey());
        Claims body = claimsJws.getBody();

        //如果token过期，返回null
        if (body.getExpiration().before(Calendar.getInstance().getTime())) {
            return null;
        }
        return JSON.parseObject(body.get(CommonConstant.JWT_USE_INFO_KEY).toString(), LoginUserInfo.class);
    }

    private static Jws<Claims> parseFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
        return claimsJws;
    }


    public static PublicKey getPublicKey() throws Exception {

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(CommonConstant.PUBLIC_KEY)
        );
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }
}
