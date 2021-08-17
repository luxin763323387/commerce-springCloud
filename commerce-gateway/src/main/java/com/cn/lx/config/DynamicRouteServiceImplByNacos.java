package com.cn.lx.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 通过 nacos 下发动态路由配置, 监听 Nacos 中路由配置变更
 * <p>
 * 如果没有nacos 可以手动编写配置，参考 {@link com.cn.lx.config.RouteLocatorConfig}
 *
 * @author StevenLu
 * @date 2021/8/8 下午11:22
 */
@Slf4j
@Component
@DependsOn("gatewayConfig")
public class DynamicRouteServiceImplByNacos {

    private final DynamicRouteServiceImpl dynamicRouteService;
    private ConfigService configService;

    public DynamicRouteServiceImplByNacos(DynamicRouteServiceImpl dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    /**
     * Bean 在容器中构造完成之后会执行 init 方法
     */
    @PostConstruct
    public void init() {
        try {
            configService = initConfigService();
            if (Objects.isNull(configService)) {
                log.error("config init fail");
                return;
            }

            //第一次启动项目，需要获取到config信息，并且添加
            String config = configService.getConfig(GatewayConfig.NACOS_ROUTE_DATA_ID,
                    GatewayConfig.NACOS_ROUTE_GROUP,
                    GatewayConfig.DEFAULT_TIMEOUT);

            log.info("初始化第一次添加的配置信息:{}", config);
            List<RouteDefinition> routeDefinitions = JSONObject.parseArray(config, RouteDefinition.class);
            routeDefinitions.forEach(dynamicRouteService::addRouteDefinition);
        } catch (Exception ex) {
            log.error("gateway route init has some error: [{}]", ex.getMessage(), ex);
        }

        //设置监听器，监听后续变更情况
        dynamicRouteByNacosListener(GatewayConfig.NACOS_ROUTE_DATA_ID, GatewayConfig.NACOS_ROUTE_GROUP);

    }

    /**
     * 初始化 Nacos Config
     *
     * @return
     */
    private ConfigService initConfigService() {

        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace", GatewayConfig.NACOS_NAMESPACE);
            ConfigService configService = NacosFactory.createConfigService(properties);
            return configService;
        } catch (Exception ex) {
            log.error("初始化nacos配置失败: [{}]", ex.getMessage(), ex);
            return null;
        }
    }

    private void dynamicRouteByNacosListener(String dataId, String groupId) {

        try {
            configService.addListener(dataId, groupId, new Listener() {

                /**
                 * <h2>自己提供线程池执行操作</h2>
                 * */
                @Override
                public Executor getExecutor() {
                    return null;
                }


                /**
                 * 监听器收到的配置更新信息
                 * @param configInfo 最新的配置信息
                 */
                @Override
                public void receiveConfigInfo(String configInfo) {

                    log.info("nacos配置信息:{}", JSON.toJSONString(configInfo));
                    List<RouteDefinition> routeDefinitions = JSONObject.parseArray(configInfo, RouteDefinition.class);
                    log.info("待更新的路由:{}", configInfo);
                    dynamicRouteService.updateList(routeDefinitions);
                }
            });

        } catch (Exception ex) {
            log.error("dynamic update gateway config error: ", ex);
        }
    }
}
