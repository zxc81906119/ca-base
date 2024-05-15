package com.redhat.cleanbaseservicesproducer.controller;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Observed
@Slf4j
public class FeignServerController {

    @RequestMapping("/test")
    public String test() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("sleep interrupted", e);
        }
        return "test";
    }

}
