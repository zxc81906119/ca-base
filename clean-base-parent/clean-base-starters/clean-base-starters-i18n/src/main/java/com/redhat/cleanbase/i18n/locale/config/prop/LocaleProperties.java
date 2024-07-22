package com.redhat.cleanbase.i18n.locale.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Data
@ConfigurationProperties("i18n.locale")
public class LocaleProperties {
    private String requestChangeLocaleParam = LocaleChangeInterceptor.DEFAULT_PARAM_NAME;
}