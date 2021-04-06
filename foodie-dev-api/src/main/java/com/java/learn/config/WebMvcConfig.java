package com.java.learn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // 实现资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // pathPatterns指定的是url访问的路径，和具体资源所在位置无关，就是一个自定义的路由
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("file:" + CustomConfig.STATIC_RESOURCE + "/");
    }
}
