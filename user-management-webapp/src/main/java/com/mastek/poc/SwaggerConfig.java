package com.mastek.poc;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.select()
        		.apis(RequestHandlerSelectors.basePackage("com.mastek.poc.controller"))
        		.paths(PathSelectors.any()).build().apiInfo(apiInfo());
    }
	
    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("User Management REST API", "User Management Rest API to Manage Organisations and Users", "1.0", "Terms of service", new Contact("vasu kaveri", "www.example.com", "vasu_kaveri@yahoo.com"), "License of API", "API license URL", Collections.emptyList());
        return apiInfo;
    }
}
