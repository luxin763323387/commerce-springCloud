package com.cn.lx.communication;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.lx.constant.CommonConstant;
import com.cn.lx.vo.JwtToken;
import com.cn.lx.vo.UserNameAndPassword;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RetryRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author StevenLu
 * @date 2021/8/31 下午10:30
 */
@Slf4j
@Component
public class UseRibbonService {

    private final RestTemplate restTemplate;

    private final DiscoveryClient discoveryClient;

    public UseRibbonService(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }


    public JwtToken getTokenFromAuthorityServiceByRibbon(UserNameAndPassword userNameAndPassword){

        // 注意到 url 中的 ip 和端口换成了服务名称
        String requestUrl = String.format("http://%s/commerce-authority-center/authority/token",
                CommonConstant.AUTHORITY_CENTER_SERVICE_ID);

        log.info("login request url and body: [{}], [{}]", requestUrl,
                JSON.toJSONString(userNameAndPassword));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 这里一定要使用自己注入的 RestTemplate
        return restTemplate.postForObject(
                requestUrl,
                new HttpEntity<>(JSON.toJSONString(userNameAndPassword), headers),
                JwtToken.class
        );
    }

    public JwtToken thinkingInRibbon(UserNameAndPassword userNameAndPassword){

        String urlFormat = "http://%s/commerce-authority-center/authority/token";
        List<ServiceInstance> instances = discoveryClient.getInstances(CommonConstant.AUTHORITY_CENTER_SERVICE_ID);

        List<Server> servers = new ArrayList<>();
        instances.forEach(e ->{
            servers.add(new Server(e.getHost(),e.getPort()));
        });
        log.info("found target instance: {}", JSON.toJSONString(servers));


        // 2. 使用负载均衡策略实现远端服务调用
        // 构建 Ribbon 负载实例
        BaseLoadBalancer loadBalancer
                = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(servers);
        //轮训策略
        loadBalancer.setRule(new RetryRule(new RandomRule(),300));

        LoadBalancerCommand<Object> loadBalancerCommand = LoadBalancerCommand.builder()
                .withLoadBalancer(loadBalancer).build();

        String result = loadBalancerCommand.submit(server -> {
            String targetUrl = String.format(
                    urlFormat,
                    String.format("%s:%s", server.getHost(), server.getPort())
            );
            log.info("target request url: [{}]", targetUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String tokenStr = new RestTemplate().postForObject(
                    targetUrl,
                    new HttpEntity<>(JSON.toJSONString(userNameAndPassword), headers),
                    String.class
            );
            return Observable.just(tokenStr);
        }).toBlocking().first().toString();

        return JSONObject.parseObject(result,JwtToken.class);

    }
}
