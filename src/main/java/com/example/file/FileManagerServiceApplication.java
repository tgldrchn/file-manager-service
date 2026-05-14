package com.example.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"}) 
public class FileManagerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileManagerServiceApplication.class, args);
    }
}