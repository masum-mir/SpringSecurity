package com.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.*;

@SpringBootApplication
@ComponentScan(basePackages = {"com.security.*"})
public class SpringRestApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpringRestApplication.class);
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(SpringRestApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "9091"));
        app.run(args);

        String[] nm = {"nm1","nm2","nm3","nm4","nm5",};

        Map<String, String> maping = new HashMap<>();

        for(String n: nm) {
            String repo = n + "_Repo";
            maping.put(n, repo);
        }

        for(String n: nm ) {
            String repoName = maping.get(n);
                System.out.println(repoName);

        }

    }

}
