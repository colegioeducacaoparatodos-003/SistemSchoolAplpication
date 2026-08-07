package com.SistemSchool.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.io.IOException;

@Configuration
public class RootRedirectConfig {

    @Bean
    public FilterRegistrationBean<Filter> rootRedirectFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                HttpServletRequest req = (HttpServletRequest) request;
                HttpServletResponse res = (HttpServletResponse) response;
                
                String uri = req.getRequestURI();
                String contextPath = req.getContextPath();
                
                // Se acessou exatamente a raiz "/", redireciona para login
                if (uri.equals(contextPath + "/")) {
                    res.sendRedirect(contextPath + "/login.xhtml");
                    return;
                }
                
                chain.doFilter(request, response);
            }
        });
        registration.addUrlPatterns("/");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}