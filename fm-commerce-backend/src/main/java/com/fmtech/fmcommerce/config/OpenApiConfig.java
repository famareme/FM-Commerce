
package com.fmtech.fmcommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fmCommerceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("FM-Commerce API")
                        .description(
                                "REST API for FM-Commerce, an e-commerce application "
                                + "built with Spring Boot and PostgreSQL."
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("FM-Tech")
                                .url("https://github.com/famareme/FM-Commerce"))
                        .license(new License()
                                .name("Private / Proprietary")
                        )
                );
    }
}
