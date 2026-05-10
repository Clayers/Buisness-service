package com.example.buisnessservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.dao.objects")
@EnableJpaRepositories(basePackages = "com.example.dao.dao")
@ComponentScan(basePackages = {
        "com.example",
        "com.example.api"
})
public class BuisnessServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuisnessServiceApplication.class, args);
    }

}
