package com.hm.tzgis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

/**
 * 处理ajax跨域请求（开发时使用）
 * @author frqky
 *
 */
//如果使用网关服务调用微服务，则注释掉Configuration注解；如果不使用网关服务，则需将Configuration注解的注释放开
@Configuration
@Order(0)
public class CorsConfig {

	@Bean
	public CorsWebFilter corsWebFilter() {
		//添加CORS配置信息
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedMethod("*");// 允许任何方法(post、get等)
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");// 允许任何的头信息
		config.setAllowCredentials(true);//是否发送Cookie信息
//		 config.setMaxAge(3600*24L);//配置有效时长

		//添加映射路径，我们拦截一切请求
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
		source.registerCorsConfiguration("/**", config);

		return new CorsWebFilter(source);
	}

}
