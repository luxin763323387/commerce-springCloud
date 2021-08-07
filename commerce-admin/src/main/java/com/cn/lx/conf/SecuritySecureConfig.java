package com.cn.lx.conf;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @author StevenLu
 * @date 2021/8/3 下午11:18
 */
@Configuration
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

    private final String adminContextPath;

    public  SecuritySecureConfig(AdminServerProperties adminContextPath){
        this.adminContextPath = adminContextPath.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        SavedRequestAwareAuthenticationSuccessHandler successHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();

        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        http.authorizeRequests()
                // 1. 配置所有的静态资源和登录页可以公开访问
                .antMatchers(adminContextPath + "/assets/**").permitAll()
                .antMatchers(adminContextPath + "/login").permitAll()
                // 2. 其他请求, 必须要经过认证
                .anyRequest().authenticated()
                .and()
                // 3. 配置登录和登出路径
                .formLogin().loginPage(adminContextPath + "/login")
                .successHandler(successHandler)
                .and()
                .logout().logoutUrl(adminContextPath + "/logout")
                .and()
                // 4. 开启 http basic 支持, 其他的服务模块注册时需要使用
                .httpBasic()
                .and()
                // 5. 开启基于 cookie 的 csrf 保护
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // 6. 忽略这些路径的 csrf 保护以便其他的模块可以实现注册
                .ignoringAntMatchers(
                        adminContextPath + "/instances",
                        adminContextPath + "/actuator/**"
                );

    }
}
