package com.example.alumnos.config;

import com.example.alumnos.entity.Autor;
import com.example.alumnos.entity.Editorial;
import com.example.alumnos.entity.Libro;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config,
            CorsRegistry cors) {

        // Base de toda la API REST
        config.setBasePath("/api");

        // MUY IMPORTANTE: exponer IDs para relaciones en frontend
        config.exposeIdsFor(
                Autor.class,
                Editorial.class,
                Libro.class
        );

        // CORS (para pruebas en navegador / frontend)
        cors.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}