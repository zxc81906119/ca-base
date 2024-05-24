package com.redhat.cleanbase.adapter.in.controller;

import com.redhat.cleanbase.common.i18n.msgsource.resolver.I18nResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/fee-tax")
@RequiredArgsConstructor
public class TestController {
    private final I18nResolver i18nResolver;

    @RequestMapping("/test")
    public String test() {
        return i18nResolver.getMessage("hello");
    }


}
