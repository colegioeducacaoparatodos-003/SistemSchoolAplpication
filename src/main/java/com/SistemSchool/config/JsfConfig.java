package com.SistemSchool.config;

import jakarta.faces.webapp.FacesServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JsfConfig implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("jakarta.faces.DEFAULT_SUFFIX", ".xhtml");
        servletContext.setInitParameter("jakarta.faces.PROJECT_STAGE", "Production");
        servletContext.setInitParameter("primefaces.THEME", "saga");
        servletContext.setInitParameter("org.apache.myfaces.NUMBER_OF_VIEWS_IN_SESSION", "25");
        servletContext.setInitParameter("org.apache.myfaces.NUMBER_OF_SEQUENTIAL_VIEWS_IN_SESSION", "5");

        var facesServlet = servletContext.addServlet("FacesServlet", FacesServlet.class);
        facesServlet.addMapping("*.xhtml", "*.jsf");
        facesServlet.setLoadOnStartup(1);
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> rootRedirect() {
        var registration = new ServletRegistrationBean<HttpServlet>();
        registration.setServlet(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                resp.sendRedirect("/login.xhtml");
            }
        });
        registration.addUrlMappings("/");
        registration.setLoadOnStartup(2);
        return registration;
    }
}