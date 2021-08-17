package com.cn.lx.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 事件推送 Aware: 动态更新路由网关 Service
 *
 * @author StevenLu
 * @date 2021/8/8 下午9:56
 */
@Service
@Slf4j
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {

    /***
     * 写路由定义
     */
    private final RouteDefinitionWriter routeDefinitionWriter;

    /**
     * 获取路由定义
     */
    private final RouteDefinitionLocator routeDefinitionLocator;

    private ApplicationEventPublisher publisher;

    public DynamicRouteServiceImpl(RouteDefinitionWriter writer,
                                   RouteDefinitionLocator locator) {
        this.routeDefinitionWriter = writer;
        this.routeDefinitionLocator = locator;
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

        //完成事件推送句柄的初始化
        this.publisher = applicationEventPublisher;
    }


    /**
     * <h2>增加路由定义</h2>
     */
    public String addRouteDefinition(RouteDefinition definition) {

        log.info("gateway add route: [{}]", definition);

        // 保存路由配置并发布
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        // 发布事件通知给 Gateway, 同步新增的路由定义
        this.publisher.publishEvent(new RefreshRoutesEvent(this));

        return "success";
    }

    /**
     * <h2>更新路由</h2>
     */
    public String updateList(List<RouteDefinition> definitions) {

        log.info("gateway update route: [{}]", definitions);

        // 先拿到当前 Gateway 中存储的路由定义
        List<RouteDefinition> routeDefinitionsExits =
                routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
        if (!CollectionUtils.isEmpty(routeDefinitionsExits)) {
            // 清除掉之前所有的 "旧的" 路由定义
            routeDefinitionsExits.forEach(rd -> {
                log.info("delete route definition: [{}]", rd);
                deleteById(rd.getId());
            });
        }

        // 把更新的路由定义同步到 gateway 中
        definitions.forEach(definition -> updateByRouteDefinition(definition));
        return "success";
    }

    /**
     * 通过路由id删除路由
     *
     * @param id
     * @return
     */
    private String deleteById(String id) {

        try {
            log.info("网关删除路由id:{}", id);
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            //发布事件通知 给gateway 更新路由定义
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "delete success";
        } catch (Exception ex) {
            log.error("gateway delete route fail:[{}]", ex.getMessage(), ex);
            return "delete fail";
        }
    }


    private String updateByRouteDefinition(RouteDefinition definition) {

        //删除
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId())).subscribe();
        } catch (Exception ex) {
            return "update fail, not find rout routeId:{}" + definition.getId();
        }

        //新增
        try {
            this.routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "update success";
        } catch (Exception ex) {
            return "save fail, not find rout routeId:{}" + definition.getId();
        }

    }


}
