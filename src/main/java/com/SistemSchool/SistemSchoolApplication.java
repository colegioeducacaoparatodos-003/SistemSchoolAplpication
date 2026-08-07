package com.SistemSchool;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;

import jakarta.faces.webapp.FacesServlet;

@SpringBootApplication
public class SistemSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemSchoolApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addContextCustomizers((Context context) -> {
            // Registra FacesServlet DIRETAMENTE no Tomcat antes do MyFaces inicializar
            Tomcat.addServlet(context, "FacesServlet", FacesServlet.class.getName());
            context.addServletMappingDecoded("*.xhtml", "FacesServlet");
            context.addServletMappingDecoded("*.jsf", "FacesServlet");
        });
    }

    @Bean
    public ServletContextInitializer jsfParamsInitializer() {
        return servletContext -> {
            servletContext.setInitParameter("jakarta.faces.PROJECT_STAGE", "Production");
            servletContext.setInitParameter("jakarta.faces.DEFAULT_SUFFIX", ".xhtml");
            servletContext.setInitParameter("primefaces.THEME", "saga");
            servletContext.setInitParameter("primefaces.UPLOADER", "commons");
        };
    }
}