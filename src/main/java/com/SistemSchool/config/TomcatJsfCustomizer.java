package com.SistemSchool.config;

import jakarta.faces.webapp.FacesServlet;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatJsfCustomizer {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> jsfServletCustomizer() {
        return factory -> factory.addInitializers(servletContext -> {
            System.out.println(">>> TOMCAT CUSTOMIZER: Registrando FacesServlet <<<");
            
            jakarta.servlet.ServletRegistration.Dynamic facesServlet = 
                servletContext.addServlet("FacesServlet", FacesServlet.class);
            facesServlet.addMapping("*.xhtml", "*.jsf");
            facesServlet.setLoadOnStartup(1);
            
            System.out.println(">>> TOMCAT CUSTOMIZER: FacesServlet registrado <<<");
        });
    }
}