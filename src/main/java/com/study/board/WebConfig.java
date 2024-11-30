package com.study.board;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // '/boardImageUpload/**' 경로로 요청된 이미지를 'C:/Temp/boardImageUpload'에서 찾도록 설정
        registry.addResourceHandler("/boardImageUpload/**")
                .addResourceLocations("file:///C:/Temp/boardImageUpload/");
        // '/userImageUpload/**' 경로로 요청된 이미지를 'C:/Temp/userImageUpload'에서 찾도록 설정
        registry.addResourceHandler("/userImageUpload/**")
                .addResourceLocations("file:///C:/Temp/userImageUpload/");
    }
}