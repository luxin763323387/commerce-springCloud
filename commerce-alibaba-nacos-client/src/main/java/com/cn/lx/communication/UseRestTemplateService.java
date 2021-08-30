package com.cn.lx.communication;

import com.alibaba.fastjson.JSON;
import com.cn.lx.constant.CommonConstant;
import com.cn.lx.vo.JwtToken;
import com.cn.lx.vo.UserNameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author StevenLu
 * @date 2021/8/30 下午10:44
 */
@Slf4j
@Component
public class UseRestTemplateService {

    private final LoadBalancerClient loadBalancerClient;

    public UseRestTemplateService(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    /**
     * 固定url通过RestTemplate获取
     *
     * @param userNameAndPassword
     * @return
     */
    public JwtToken getJwtTokenFromAuthorityService(UserNameAndPassword userNameAndPassword) {
        String requestUrl = "http://127.0.0.1:7000/commerce-authority-center" +
                "/authority/token";
        log.info("RestTemplate request url and body: [{}], [{}]",
                requestUrl, JSON.toJSONString(userNameAndPassword));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RestTemplate().postForObject(requestUrl,
                new HttpEntity<>(JSON.toJSONString(userNameAndPassword), headers),
                JwtToken.class);
    }

    /**
     * 通过注册中心获取实例，且负载均衡
     * @param userNameAndPassword
     * @return
     */
    public JwtToken getJwtTokenWithLoadBalancer(UserNameAndPassword userNameAndPassword) {

        // 第二种方式: 通过注册中心拿到服务的信息(是所有的实例), 再去发起调用
        ServiceInstance serviceInstance = loadBalancerClient.choose(CommonConstant.AUTHORITY_CENTER_SERVICE_ID);

        String requestUrl = String.format("http://%s:%s/commerce-authority-center" + "/authority/token",
                serviceInstance.getHost(),
                serviceInstance.getPort());
        log.info("RestTemplate request url and body: [{}], [{}]",
                requestUrl, JSON.toJSONString(userNameAndPassword));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RestTemplate().postForObject(requestUrl,
                new HttpEntity<>(JSON.toJSONString(userNameAndPassword), headers),
                JwtToken.class);

    }


}
