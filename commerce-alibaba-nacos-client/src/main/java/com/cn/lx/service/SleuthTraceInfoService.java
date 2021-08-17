package com.cn.lx.service;

import brave.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author StevenLu
 * @date 2021/8/15 下午2:45
 */
@Slf4j
@Component
public class SleuthTraceInfoService {

    /**
     * 通过 breve 获取信息
     */
    private final Tracer tracer;

    public SleuthTraceInfoService(Tracer tracer) {
        this.tracer = tracer;
    }


    public void logCurrentTraceInfo() {
        log.info("Sleuth trace id [{}]", tracer.currentSpan().context().traceId());
        log.info("Sleuth span id [{}]", tracer.currentSpan().context().spanId());
    }


}
