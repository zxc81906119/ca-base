package com.redhat.cleanbase.account;

import com.redhat.cleanbase.common.annotation.EnableCommon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCommon
@SpringBootApplication
public class CleanBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(CleanBaseApplication.class, args);
    }

}
