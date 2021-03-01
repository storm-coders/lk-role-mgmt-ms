package com.vcgdev.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.any;

@Configuration
@EnableSwagger2

public class SwaggerConfig {
    
    private final Contact contact = new Contact("Victor de la Cruz", "https://www.codementor.io/@vcg_cruz", ""); 

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()                 
                .apis(RequestHandlerSelectors.basePackage("com.vcgdev.demo.rest"))
                .paths(any())
                .build()
                .apiInfo(metaData());
             
	}
	
	private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Demo API")
                .description("Demo Description")
                .version("0.0.1")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .contact(contact)
                .build();
	}
}
