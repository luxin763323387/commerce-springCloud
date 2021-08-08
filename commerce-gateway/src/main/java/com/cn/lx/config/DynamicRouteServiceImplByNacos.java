package com.cn.lx.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author StevenLu
 * @date 2021/8/8 下午11:22
 */
@Slf4j
@Component
@DependsOn("gatewayConfig")
public class DynamicRouteServiceImplByNacos {

    private final DynamicRouteServiceImpl dynamicRouteService;

    public DynamicRouteServiceImplByNacos(DynamicRouteServiceImpl dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }


    /**
     * 初始化 Nacos Config
     * @return
     */
    private ConfigService initConfigService(){

        try{
            Properties properties = new Properties();
            properties.setProperty("serverAddr",GatewayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace",GatewayConfig.NACOS_NAMESPACE);
            ConfigService configService = NacosFactory.createConfigService(properties);
            return configService;
        }catch (Exception ex){
            log.error("初始化nacos配置失败: [{}]", ex.getMessage(), ex);
            return null;
        }
    }






}
