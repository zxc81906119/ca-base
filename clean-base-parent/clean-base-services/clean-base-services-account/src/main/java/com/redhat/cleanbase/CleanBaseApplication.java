package com.redhat.cleanbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootApplication
public class CleanBaseApplication {
    @ExceptionHandler
    public static void main(String[] args) {
        SpringApplication.run(CleanBaseApplication.class, args);
    }

}
