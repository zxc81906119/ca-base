package com.redhat.cleanbase.adapter.in.controller;

import com.redhat.cleanbase.common.i18n.msg_source.input.GenericI18nInput;
import com.redhat.cleanbase.common.i18n.msg_source.ConvenientMsgSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/fee-tax")
@RequiredArgsConstructor
public class TestController {
    private final ConvenientMsgSource convenientMsgSource;

    @RequestMapping("/test")
    public String test() {
        return convenientMsgSource.getMessage(new GenericI18nInput("hello"));
    }


}
