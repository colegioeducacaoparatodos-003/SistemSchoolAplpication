package com.SistemSchool;

import jakarta.faces.webapp.FacesServlet;
import org.apache.myfaces.webapp.StartupServletContextListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SistemSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemSchoolApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean<FacesServlet> facesServlet() {
        return new ServletRegistrationBean<>(new FacesServlet(), "*.xhtml", "*.jsf");
    }

    @Bean
    public ServletListenerRegistrationBean<StartupServletContextListener> myFacesStartupListener() {
        return new ServletListenerRegistrationBean<>(new StartupServletContextListener());
    }

    @Bean
    public ServletContextInitializer initializer() {
        return servletContext -> {
            servletContext.setInitParameter("jakarta.faces.PROJECT_STAGE", "Production");
            servletContext.setInitParameter("jakarta.faces.DEFAULT_SUFFIX", ".xhtml");
            servletContext.setInitParameter("primefaces.THEME", "saga");
            servletContext.setInitParameter("primefaces.UPLOADER", "commons");
        };
    }
}