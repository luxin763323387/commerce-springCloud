package com.cn.lx.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * 将实现的局部过滤器，假如到过滤器工程中
 *
 * @author StevenLu
 * @date 2021/8/11 下午11:13
 */
@Component
public class HeaderTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return new HeadTokenGatewayFilter();
    }
}
