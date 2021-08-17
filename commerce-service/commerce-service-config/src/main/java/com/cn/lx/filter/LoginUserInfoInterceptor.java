package com.cn.lx.filter;

import com.alibaba.fastjson.JSON;
import com.cn.lx.constant.CommonConstant;
import com.cn.lx.util.TokenParseUtil;
import com.cn.lx.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 用户身份统一登录拦截
 *
 * @author StevenLu
 * @date 2021/8/17 下午10:27
 */
@Slf4j
@Component
public class LoginUserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        if(checkedWhiteUrl(request.getRequestURI())){
            return true;
        }

        String token = request.getHeader(CommonConstant.JWT_USE_INFO_KEY);
        LoginUserInfo loginUserInfo = null;
        try {
            loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        } catch (Exception ex) {
            log.error("parse login user info error: [{}]", ex.getMessage(), ex);
        }

        if (Objects.isNull(loginUserInfo)){
            log.error("不能解析当前用户信息-参数:{}", JSON.toJSONString(loginUserInfo));
            throw  new RuntimeException("不能解析当前用户信息");
        }

        AccessContext.setLoginUserInfo(loginUserInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //用完删除
        if(Objects.nonNull(AccessContext.getLoginUserInfo())){
            AccessContext.remove();
        }
    }

    private boolean checkedWhiteUrl(String url){
        return StringUtils.containsAny(
                url,
                "springfox", "swagger", "v2",
                "webjars", "doc.html"
        );
    }
}
