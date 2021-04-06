package com.example.rs256;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = "com.example.rs256.repository")
public class Rs256Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Rs256Application.class, args);
    }

}
