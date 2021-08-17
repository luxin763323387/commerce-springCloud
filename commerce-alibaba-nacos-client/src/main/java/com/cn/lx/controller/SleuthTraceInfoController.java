package com.cn.lx.controller;

import com.cn.lx.service.SleuthTraceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 打印跟踪信息
 *
 * @author StevenLu
 * @date 2021/8/15 下午2:50
 */
@Slf4j
@RestController
@RequestMapping("/sleuth")
public class SleuthTraceInfoController {

    private final SleuthTraceInfoService traceInfoService;

    public SleuthTraceInfoController(SleuthTraceInfoService sleuthTraceInfoService) {
        this.traceInfoService = sleuthTraceInfoService;
    }


    /**
     * <h2>打印日志跟踪信息</h2>
     */
    @GetMapping("/trace-info")
    public void logCurrentTraceInfo() {
        traceInfoService.logCurrentTraceInfo();
    }
}
