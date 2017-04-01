package org.daimler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger auto configuration generates docs for api
 *
 * @author Abhilash.ghosh
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(SwaggerAPIProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.daimler.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo(properties));
    }

    private ApiInfo apiInfo(SwaggerAPIProperties properties) {
        Contact contact = new Contact("Abhilash Ghosh", "", "rivu007@gmail.com");
        return new ApiInfo(
                properties.getTitle(),
                properties.getDescription(),
                "API HeyBeach",
                properties.getTermsOfServiceUrl(),
                contact,
                properties.getLicense(),
                properties.getLicenseUrl());
    }
}
