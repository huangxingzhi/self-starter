package com.springboot;

import com.springboot.autoconfig.EnableHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableHttpClient
@SpringBootApplication
public class SelfStarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(SelfStarterApplication.class, args);
    }
}
