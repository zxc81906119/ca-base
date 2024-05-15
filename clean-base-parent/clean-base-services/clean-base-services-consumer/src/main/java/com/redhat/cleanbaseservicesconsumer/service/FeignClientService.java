package com.redhat.cleanbaseservicesconsumer.service;

import org.springframework.cloud.openfeign.FallbackFactory;
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

@Component
class TestFallbackFactory implements FallbackFactory<FallbackWithFactory> {

    @Override
    public FallbackWithFactory create(Throwable cause) {
        return new FallbackWithFactory();
    }

}

class FallbackWithFactory implements FeignClientService {

    //8083
    @Override
    public String test83() {
        return "No test83";
    }

}