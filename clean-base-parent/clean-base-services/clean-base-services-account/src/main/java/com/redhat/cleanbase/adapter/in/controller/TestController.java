package com.redhat.cleanbase.adapter.in.controller;

import com.redhat.cleanbase.common.i18n.msg_source.input.GenericI18nInput;
import com.redhat.cleanbase.common.i18n.msg_source.ConvenientMsgSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final ConvenientMsgSource convenientMsgSource;

    @RequestMapping("/test")
    public String test() {
        return convenientMsgSource.getMessage(new GenericI18nInput("hello"));
    }

    @RequestMapping(value = "/test1", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> test1(@RequestBody Map<String, Object> map) {
        log.info("data: {}", map);
        return map;
    }

}
