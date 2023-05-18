package com.neo.exp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class ConfigSwagger {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.neocortex"))
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
        // http://localhost:8080/api/swagger-ui/index.html
    }

    private ApiInfo apiInfo () {
        return new ApiInfoBuilder()
                .title("Swagger Configuration for Neocortex Storage Service")
                .description("\"Spring Boot Swagger configuration\"")
                .version("1.1.0").build();
    }
}
