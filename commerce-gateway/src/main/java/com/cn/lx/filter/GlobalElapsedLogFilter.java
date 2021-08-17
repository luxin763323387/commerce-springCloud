package com.cn.lx.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * 全局接口耗时日志过滤器
 *
 * @author StevenLu
 * @date 2021/8/15 上午11:32
 */
@Slf4j
@Component
public class GlobalElapsedLogFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        StopWatch sw = StopWatch.createStarted();
        URI uri = exchange.getRequest().getURI();
        Mono<Void> mono = chain.filter(exchange).then(
                Mono.fromRunnable(() ->
                        log.info("[{}] elapsed: [{}ms]",
                                uri, sw.getTime(TimeUnit.MILLISECONDS))
                )
        );
        return mono;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
