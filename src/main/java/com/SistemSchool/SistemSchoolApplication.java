package com.SistemSchool;

import jakarta.faces.webapp.FacesServlet;
import jakarta.servlet.ServletRegistration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SistemSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemSchoolApplication.class, args);
    }

    @Bean
    public ServletContextInitializer jsfInitializer() {
        return servletContext -> {
            // Registra o FacesServlet DIRETAMENTE no Tomcat (antes do MyFaces inicializar)
            ServletRegistration.Dynamic facesServlet = servletContext.addServlet("FacesServlet", FacesServlet.class);
            facesServlet.addMapping("*.xhtml", "*.jsf");
            facesServlet.setLoadOnStartup(1);

            // Parâmetros do JSF/MyFaces
            servletContext.setInitParameter("jakarta.faces.PROJECT_STAGE", "Production");
            servletContext.setInitParameter("jakarta.faces.DEFAULT_SUFFIX", ".xhtml");
            servletContext.setInitParameter("primefaces.THEME", "saga");
            servletContext.setInitParameter("primefaces.UPLOADER", "commons");
        };
    }
}