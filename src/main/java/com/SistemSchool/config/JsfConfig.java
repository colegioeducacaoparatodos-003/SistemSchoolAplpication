package com.SistemSchool.config;

import jakarta.faces.webapp.FacesServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JsfConfig {

    @Bean
    public DispatcherServletPath dispatcherServletPath() {
        return () -> "/api";
    }

    @Bean
    public ServletRegistrationBean<FacesServlet> facesServlet() {
        ServletRegistrationBean<FacesServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new FacesServlet());
        registration.addUrlMappings("*.xhtml", "*.jsf");
        registration.setLoadOnStartup(1);
        registration.setName("FacesServlet");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> rootRedirectServlet() {
        ServletRegistrationBean<HttpServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                resp.sendRedirect("/login.xhtml");
            }
        });
        registration.addUrlMappings("/");
        registration.setName("RootRedirect");
        registration.setLoadOnStartup(2);
        return registration;
    }
}