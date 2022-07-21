package com.hm.tzgis.config;


import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/** * XSS过滤器 * @author zsy */
@WebFilter(filterName="xssFilter",urlPatterns="/*")
@Order(1)
public class XssFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest; String path = request.getServletPath();
        //由于我的@WebFilter注解配置的是urlPatterns="/*"(过滤所有请求),所以这里对不需要过滤的静态资源url,作忽略处理(大家可以依照具体需求配置)
        String[] exclusionsUrls = {".js",".gif",".jpg",".png",".css",".ico"};
        for (String str : exclusionsUrls) {
            if (path.contains(str)) {
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }
        }

        filterChain.doFilter(new XssHttpServletRequestWrapper(request),servletResponse);
    }
    @Override public void destroy() {

    }
}