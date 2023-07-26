package com.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
@ComponentScan(basePackages = {"com.security.*"})
@EnableSwagger2
public class SpringRestApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(SpringRestApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "9091"));
        app.run(args);

        System.out.println("Random Number :: "+(int)(Math.random()*(100-50+1)+50));
    }

//    @Bean
//    public Docket productApi() {
//        return new Docket(DocumentationType.SWAGGER_2).groupName("Swagger Test")
//                .apiInfo(apiInfo("Api","1.0.0","Test"))
//                .select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build()
//                .globalOperationParameters(Arrays.asList(new ParameterBuilder().name("Test2").modelRef(new ModelRef("string"))
//                        .parameterType("header").required(true).build()));
//
//    }
//
//    private ApiInfo apiInfo(String title, String version, String description) {
//        return new ApiInfoBuilder().title(title)
//                .description(description)
//                .version(version)
//                .build();
//    }

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("Swagger Test")
                .apiInfo(apiInfo("API", "1.0.0", "Test"))
                .select().apis(RequestHandlerSelectors.basePackage("com.security"))
                .paths(PathSelectors.any()).build()
                .globalOperationParameters(Collections.singletonList(new ParameterBuilder()
                        .name("Authorization")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(true)
                        .defaultValue("Bearer your-access-token")
                        .build()))
                .enable(true);
    }

    private ApiInfo apiInfo(String title, String version, String description) {
        return new ApiInfoBuilder().title(title)
                .description(description)
                .version(version)
                .build();
    }

}
