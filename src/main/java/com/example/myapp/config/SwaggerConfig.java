package com.example.myapp.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(getApiInfo())
                .externalDocs(new ExternalDocumentation()
                        .description("Own Finance Project")
                        .url("N/A"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    private Info getApiInfo() {
        Contact newContact = new Contact();
        newContact.setName("pankaj");
        newContact.setEmail("pankajdoodhwal0084395@gmail.com");
        newContact.setUrl("N/A");

        License license = new License();
        license.setName("N/A");
        license.setUrl("N/A");

        return new Info()
                .title("Own Finance Project")
                .description("This is used to manage my own finances")
                .version("1.0")
                .termsOfService("N/A")
                .contact(newContact)
                .license(license);
    }


}