package com.example.kokoni.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Kokoni Manga Tracker API",
        description = "Plataforma para el seguimiento de lectura de Mangas y Cómics.",
        version = "1.0.0",
        contact = @Contact(
            name = "Kokoni Team",
            url = "https://github.com/FemCoders",
            email = "support@kokoni.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    ),
    servers = @Server(
        description = "Servidor de Desarrollo",
        url = "http://localhost:8080"
    )
)
public class SwaggerConfig {
}
