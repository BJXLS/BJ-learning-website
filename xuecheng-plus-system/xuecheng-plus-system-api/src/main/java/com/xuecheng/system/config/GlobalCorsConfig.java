package com.xuecheng.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Hao Ge
 * @version 1.0
 * @description 跨域过滤器
 * @date 2023/1/26 23:20
 */
@Configuration
public class GlobalCorsConfig {

    /***
     * @description 允许跨域
     *
     * @return org.springframework.web.filter.CorsFilter
     * @author Hao Ge
     * @date 2023/1/26 23:28*/
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许白名单域名进行跨域调用
        config.addAllowedOrigin("*");
        // 允许跨域发送cookie
        config.setAllowCredentials(true);
        // 放行全部原始头信息
        config.addAllowedHeader("*");
        // 允许所有请求方法跨域
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
