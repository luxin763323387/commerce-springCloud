package com.cn.lx.communication;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author StevenLu
 * @date 2021/8/31 下午10:23
 */
@Component
public class RibbonConfig {

    /**
     * <h2>注入 RestTemplate</h2>
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
