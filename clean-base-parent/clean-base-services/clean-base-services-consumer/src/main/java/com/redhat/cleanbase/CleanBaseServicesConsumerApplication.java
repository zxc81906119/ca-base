package com.redhat.cleanbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CleanBaseServicesConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CleanBaseServicesConsumerApplication.class, args);
    }

}
