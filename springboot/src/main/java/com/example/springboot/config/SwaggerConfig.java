package com.example.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info().title("API Test")
                .description("Swagger UI practice")
                .version("1.0.0");
    }

}
