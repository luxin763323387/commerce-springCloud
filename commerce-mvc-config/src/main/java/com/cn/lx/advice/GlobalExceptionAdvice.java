package com.cn.lx.advice;

import com.cn.lx.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常
 *
 * @author StevenLu
 * @date 2021/7/26 上午9:32
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public CommonResponse<String> handlerCommerceException(HttpServletRequest request, Exception ex) {
        CommonResponse<String> response = new CommonResponse<>(-1, "business error");
        response.setDate(ex.getMessage());
        log.error("commerce service has error:[{}]",ex.getMessage(),ex);
        return response;
    }
}
