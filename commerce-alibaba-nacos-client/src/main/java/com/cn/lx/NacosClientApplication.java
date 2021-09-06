package com.cn.lx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author StevenLu
 * @date 2021/7/29 下午10:45
 */
@EnableFeignClients
@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
public class NacosClientApplication {

    public static void main(String[] arg) {
        SpringApplication.run(NacosClientApplication.class, arg);
    }
}
