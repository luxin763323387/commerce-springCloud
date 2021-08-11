package com.cn.lx.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Http 请求头带 token校验
 *
 * @author StevenLu
 * @date 2021/8/11 下午11:03
 */
public class HeadTokenGatewayFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //从Http Header中获取 为 key 为token， value 的 imooc 的键值对
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (Objects.equals(token, "imooc")) {
            return chain.filter(exchange);
        }
        // 标记此次请求没有权限，并且结束请求
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        Mono<Void> voidMono = exchange.getResponse().setComplete();
        return voidMono;
    }

    /**
     * 最高优先级+2
     *
     * @return
     */
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }
}
