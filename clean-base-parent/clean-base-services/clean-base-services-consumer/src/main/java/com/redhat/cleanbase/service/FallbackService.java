package com.redhat.cleanbase.service;

public class FallbackService implements FeignClientService {

    //8083
    @Override
    public String test83() {
        return "No test83";
    }

}
