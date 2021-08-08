package com.cn.lx;

import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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



}
