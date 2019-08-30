package com.phhc.sso.config;

import com.phhc.sso.config.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置类
 * @author xieym
 * @date 2019/8/29
 * @since
 **/
@Component
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;
    /**
     * 这个方法用来注册拦截器，写好的拦截器需要通过这里添加注册才能生效
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         *  添加登录拦截器到容器
         *  addPathPatterns("/**") 表示拦截所有的请求
         *  excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
         **/
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/login", "/register");
    }
}
