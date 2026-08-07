package com.SistemSchool;

import jakarta.faces.webapp.FacesServlet;
import jakarta.servlet.ServletRegistration;
import org.apache.myfaces.webapp.FacesInitializerImpl;
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
    public ServletContextInitializer facesContextInitializer() {
        return servletContext -> {
            // 1. Parâmetros do JSF
            servletContext.setInitParameter("jakarta.faces.PROJECT_STAGE", "Production");
            servletContext.setInitParameter("jakarta.faces.DEFAULT_SUFFIX", ".xhtml");
            servletContext.setInitParameter("primefaces.THEME", "saga");
            servletContext.setInitParameter("primefaces.UPLOADER", "commons");

            // 2. Registra o FacesServlet
            ServletRegistration.Dynamic facesServlet = servletContext.addServlet("FacesServlet", FacesServlet.class);
            facesServlet.addMapping("*.xhtml", "*.jsf");
            facesServlet.setLoadOnStartup(1);

            // 3. INICIALIZA O MYFACES MANUALMENTE (o ServletContainerInitializer não funciona em fat JAR)
            new FacesInitializerImpl().initFaces(servletContext);
        };
    }
}