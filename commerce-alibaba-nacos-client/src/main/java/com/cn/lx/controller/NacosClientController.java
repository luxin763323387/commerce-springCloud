package com.cn.lx.controller;

import com.cn.lx.service.NacosClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author StevenLu
 * @date 2021/7/29 下午11:13
 */

@Slf4j
@RestController
@RequestMapping("/nacos-client")
public class NacosClientController {

    @Autowired
    private NacosClientService nacosClientService;

    @GetMapping("/service-instance")
    List<ServiceInstance> logNamcosClientInfo(@RequestParam(defaultValue = "commerce-nacos-client") String serviceId) {
        return nacosClientService.getNacosClientInfo(serviceId);
    }

}
