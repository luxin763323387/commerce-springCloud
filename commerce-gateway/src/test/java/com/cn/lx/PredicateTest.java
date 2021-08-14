package com.cn.lx;

import com.alibaba.fastjson.JSON;
import com.cn.lx.vo.JwtToken;
import com.cn.lx.vo.UserNameAndPassword;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author StevenLu
 * @date 2021/8/8 下午5:13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PredicateTest {
    @Autowired
    private RestTemplate restTemplate;

    private static List<String> list = Arrays.asList("nacos", "gateway", "authority", "config",null);

    @Test
    public void predicateTest() {
        Predicate<String> stringPredicate = s -> s.length() > 5;
        list.stream().filter(stringPredicate).forEach(System.out::println);
    }


    @Test
    public void predicateAddTest(){
        Predicate<String> stringPredicate = s ->s.length() >5;
        Predicate<String> stringPredicate1 = s -> s.startsWith("gate");
        list.stream().filter(stringPredicate.and(stringPredicate1)).forEach(System.out::println);
    }


    @Test
    public void predicateIsEqualTest(){

        Predicate<String> predicate = s ->Predicate.isEqual(null).test(s);
        list.stream().filter(predicate).forEach(System.out::println);
    }

    @Test
    public void restTemplateTest(){
        String requestUrl = "http://192.168.1.13:7000/commerce-authority-center/authority/token";
        UserNameAndPassword requestBody = new UserNameAndPassword();
        requestBody.setPassword("e10adc3949ba59abbe56e057f20f883e");
        requestBody.setUserName("luxin");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JwtToken token = restTemplate.postForObject(
                requestUrl,
                new HttpEntity<>(JSON.toJSONString(requestBody), headers),
                JwtToken.class
        );



        System.out.println("1");
        System.out.println(token);

    }


}
