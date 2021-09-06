package com.cn.lx.communication;

import com.cn.lx.vo.JwtToken;
import com.cn.lx.vo.UserNameAndPassword;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author StevenLu
 * @date 2021/9/2 下午11:08
 */
@FeignClient(contextId = "AuthorityFeignClient", value = "commerce-authority-center")
public interface AuthorityFeignClient {

    @PostMapping(value = "/commerce-authority-center/authority/token",
            consumes = "application/json", produces = "application/json")
    public JwtToken token(@RequestBody UserNameAndPassword userNameAndPassword);
}
