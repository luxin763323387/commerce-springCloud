package com.cn.lx.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author StevenLu
 * @date 2021/7/29 下午11:09
 */
@Slf4j
@Component
public class NacosClientService {

    @Autowired
    private DiscoveryClient discoveryClient;


    /**
     * 打印Nacos Client 信息到日志中
     *
     * @param serviceId
     * @return
     */
    public List<ServiceInstance> getNacosClientInfo(String serviceId) {
        log.info("服务客户端请求的服务id:{}", serviceId);
        return discoveryClient.getInstances(serviceId);
    }
}
