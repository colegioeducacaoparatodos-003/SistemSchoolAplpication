package com.SistemSchool.config;

import jakarta.faces.webapp.FacesServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JsfConfig {

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                System.out.println(">>> REGISTRANDO FacesServlet no ServletContext <<<");
                
                ServletRegistration.Dynamic facesServlet = servletContext.addServlet("FacesServlet", FacesServlet.class);
                facesServlet.addMapping("*.xhtml", "*.jsf");
                facesServlet.setLoadOnStartup(1);
                
                System.out.println(">>> FacesServlet REGISTRADO com sucesso <<<");
            }
        };
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