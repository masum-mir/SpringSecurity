package com.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;

@SpringBootApplication
@ComponentScan(basePackages = {"com.security.*"})
public class SpringRestApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(SpringRestApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "9091"));
        app.run(args);
    }

}
