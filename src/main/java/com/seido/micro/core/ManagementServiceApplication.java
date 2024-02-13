package com.seido.micro.core;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Spring boot application
 */
@ComponentScan(basePackages = "com.seido.micro.core")
@SpringBootApplication
@EntityScan("com.seido.micro.core.back.model")
@EnableJpaRepositories("com.seido.micro.core.back.repository")
public class ManagementServiceApplication {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Spring boot launch method
     * @param args String[]
     */
    public static void main(String[] args) {
        SpringApplication.run(ManagementServiceApplication.class, args);
    }

}
