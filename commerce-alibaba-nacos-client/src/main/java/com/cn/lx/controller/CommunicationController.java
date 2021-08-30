package com.cn.lx.controller;

import com.cn.lx.communication.UseRestTemplateService;
import com.cn.lx.vo.JwtToken;
import com.cn.lx.vo.UserNameAndPassword;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author StevenLu
 * @date 2021/8/30 下午11:20
 */
@RestController
@RequestMapping("/communication")
public class CommunicationController {

    private final UseRestTemplateService restTemplateService;

    public CommunicationController(UseRestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    @PostMapping("/rest-template")
    public JwtToken getTokenFromAuthorityService(
            @RequestBody UserNameAndPassword usernameAndPassword) {
        return restTemplateService.getJwtTokenFromAuthorityService(usernameAndPassword);
    }

    @PostMapping("/rest-template-load-balancer")
    public JwtToken getTokenFromAuthorityServiceWithLoadBalancer(
            @RequestBody UserNameAndPassword usernameAndPassword) {
        return restTemplateService.getJwtTokenWithLoadBalancer(
                usernameAndPassword);
    }
/*
    @PostMapping("/ribbon")
    public JwtToken getTokenFromAuthorityServiceByRibbon(
            @RequestBody UserNameAndPassword usernameAndPassword) {
        return ribbonService.getTokenFromAuthorityServiceByRibbon(usernameAndPassword);
    }

    @PostMapping("/thinking-in-ribbon")
    public JwtToken thinkingInRibbon(@RequestBody UserNameAndPassword usernameAndPassword) {
        return ribbonService.thinkingInRibbon(usernameAndPassword);
    }

    @PostMapping("/token-by-feign")
    public JwtToken getTokenByFeign(@RequestBody UserNameAndPassword usernameAndPassword) {
        return feignClient.getTokenByFeign(usernameAndPassword);
    }

    @PostMapping("/thinking-in-feign")
    public JwtToken thinkingInFeign(@RequestBody UserNameAndPassword usernameAndPassword) {
        return useFeignApi.thinkingInFeign(usernameAndPassword);
    }*/
}
