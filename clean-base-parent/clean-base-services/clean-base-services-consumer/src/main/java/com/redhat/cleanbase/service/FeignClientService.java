package com.redhat.cleanbase.service;

import com.redhat.cleanbase.factory.TestFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(contextId = "cbYes", name="clean-base-services-producer", url="${producer.url}", fallbackFactory = TestFallbackFactory.class)
@Component
public interface FeignClientService {

    //8083
    @RequestMapping(value ="/test")
    String test83();

}

