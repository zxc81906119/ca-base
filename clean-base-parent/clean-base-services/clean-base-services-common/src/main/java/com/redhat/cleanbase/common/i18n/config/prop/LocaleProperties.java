package com.redhat.cleanbase.common.i18n.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Data
@ConfigurationProperties("i18n.locale")
public class LocaleProperties {
    private Locale defaultLocale = Locale.TAIWAN;
    private String requestChangeLocaleParam = LocaleChangeInterceptor.DEFAULT_PARAM_NAME;
}