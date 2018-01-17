package com.aiyolo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/launch").setViewName("launch");
//        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/error").setViewName("error");
        registry.addViewController("/403").setViewName("403");
        registry.addViewController("/404").setViewName("404");

        registry.addViewController("/gateway").setViewName("gateway");
        registry.addViewController("/alarm").setViewName("alarm");
        registry.addViewController("/news").setViewName("news");

        registry.addViewController("/user").setViewName("user");
        registry.addViewController("/channel").setViewName("channel");
        registry.addViewController("/log").setViewName("log");
        registry.addViewController("/profile").setViewName("changePassword");
    }

}
