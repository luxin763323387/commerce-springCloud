package com.cn.lx.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 网关需要注入到容器中的 Bean
 *
 * @author StevenLu
 * @date 2021/8/14 下午9:46
 */
@Configuration
public class GatewayBeanConf {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
